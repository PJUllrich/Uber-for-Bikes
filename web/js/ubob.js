var ubob = angular.module('ubobApp', ['ngMaterial', 'ngAnimate', 'angularModalService', 'ngMap', 'ui.bootstrap']);

ubob.service('BikeService', function() {
    this.bikes = [];

    this.addBike = function (bike) {
        this.bikes.push(bike);
    };

    this.removeBike = function (bike) {
        this.bikes.splice(bikeIndexInArray(this.bikes, bike.id), 1);
    };

    this.updateBike = function (bike) {
        this.bikes.splice(bikeIndexInArray(this.bikes, bike.id), 1);
        this.bikes.push(bike);
    };
});

ubob.service('UserService', function () {
    this.id = 0;
    this.username = "";
    this.balance = 0;
    this.rented = [];
});

ubob.service('ServletService', function (UserService) {
    this.loginAtServer = function ($http, username, password) {
        return $http({
            method: 'POST',
            url: 'servlet',
            data: $.param({
                action: "validateLogin",
                name: username,
                password: password
            }),
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        })
            .success(function (result) {
                return parseResult(UserService, result);
            });
    };

    this.createUserAtServer = function ($http, username, password) {
        return $http({
            method: 'POST',
            url: 'servlet',
            data: $.param({
                action: "createUser",
                name: username,
                password: password
            }),
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        })
            .success(function (result) {
                return parseResult(UserService, result);
            });
    }
});

ubob.service('WebsocketService', function ($rootScope, BikeService, UserService) {
    var socket = new WebSocket("ws://" + window.location.host + window.location.pathname + "actions");
    socket.onmessage = onMessage;

    function onMessage(event) {
        $rootScope.$apply(function () {
            var message = JSON.parse(event.data);
            switch (message.action) {
                case "addBike":
                    BikeService.addBike(parseBike(message));
                    break;
                case "removeBike":
                    BikeService.removeBike(parseBike(message));
                    break;
                case "updateUser":
                    UserService.username = message.username;
                    UserService.balance = message.balance;
                    UserService.rented = JSON.parse(message.rented);
                    break;
                case "updateBike":
                    BikeService.updateBike(parseBike(message));
                    break;
                default:
                    console.log("Could not verify which action was send.");
                    break;
            }
        });
    }

    function parseBike(message) {
        var location = JSON.parse(message.location);
        return new Bike(message.id, location, message.available);
    }

    this.addRandomBike = function () {
        var data = {
            action: "addBike"
        };

        socket.send(JSON.stringify(data));
    };

    this.changeBikeRental = function (bike, rent, id) {
        var data = {
            action: "changeBikeRental",
            rent: rent,
            userID: id,
            bikeID: bike.id
        };

        socket.send(JSON.stringify(data));
    };

    this.getAllBikes = function () {
        var data = {
            action: "getAllBikes"
        };

        this.waitForConnection(function () {
            socket.send(JSON.stringify(data));
        }, 100);
    };

    this.waitForConnection = function (callback, interval) {
        if (socket.readyState === 1) {
            callback();
        } else {
            var self = this;
            setTimeout(function () {
                self.waitForConnection(callback, interval);
            }, interval);
        }
    };

    // Call this to get all bikes when the website loads.
    // Has to be put behind the getAllBikes() function and the waitForConnection() function
    this.getAllBikes();
});

ubob.controller('MapCtrl', function ($scope, BikeService, UserService, NgMap) {
    NgMap.getMap().then(function (map) {
        $scope.map = map;
    });

    $scope.normalImage = {
        url: 'img/bike64.png',
        size: [64, 64],
        origin: [0, 0],
        anchor: [0, 32]
    };
    $scope.rentedImage = {
        url: 'img/bikeRented64.png',
        size: [64, 64],
        origin: [0, 0],
        anchor: [0, 32]
    };

    $scope.bikeService = BikeService;
    $scope.userService = UserService;

    $scope.showBikeDetails = function (event, bike) {
        $scope.selectedBike = bike;
        $scope.map.showInfoWindow('markerInfoWindow', this);
    };

    $scope.userIsLoggedIn = function () {
        return UserService.username != "";
    };

    $scope.bikeRentedByUser = function (bike) {
        return bikeIndexInArray(UserService.rented, bike.id) != null;
    };
});

ubob.controller('UserCtrl', function ($scope, UserService) {
    $scope.service = UserService;
});

ubob.controller('ButtonCtrl', function ($scope, ModalService, BikeService, WebsocketService, UserService) {
    $scope.username = null;
    $scope.password = null;

    $scope.showLoginModal = function() {
        ModalService.showModal({
            templateUrl: "loginModal.jsp",
            controller: 'LoginModalCtrl'
        }).then(function(modal) {
            modal.element.modal();
        });
    };

    $scope.showRegisterModal = function() {
        ModalService.showModal({
            templateUrl: "subscribeModal.jsp",
            controller: 'SubscribeModalCtrl'
        }).then(function(modal) {
            modal.element.modal();
            modal.close.then(function(result) {
                console.log("Successfully created an user!");
            });
        });
    };

    $scope.remoteAddRandomBike = function () {
        WebsocketService.addRandomBike()
    };

    $scope.changeBikeRental = function (bike, rent) {
        WebsocketService.changeBikeRental(bike, rent, UserService.id);
    };
});

ubob.controller('LoginModalCtrl', function ($scope, $http, $element, ServletService, close) {
    $scope.username = '';
    $scope.password = '';

    $scope.login = function(){
        var validInput = true;

        $scope.usernameRequired = null;
        $scope.passwordRequired = null;

        if ($scope.username == '') {
            $scope.usernameRequired = "A username is required.";
            validInput = false;
        }

        if ($scope.password == '') {
            $scope.passwordRequired = "A password is required.";
            validInput = false;
        }

        if (validInput) {
            var success = ServletService.loginAtServer($http, $scope.username, $scope.password);
            success.then(function (result) {
                if (result.data["success"] == "true") {
                    $scope.errorMessage = "";
                    $element.modal('hide');
                    close({
                        username: $scope.username,
                        password: $scope.password
                    }, 500);
                } else {
                    $scope.errorMessage = "Password and/or Username are wrong.";
                }
            });
        }
    };

    $scope.cancel = function() {
        $element.modal('hide');

        close({
            username: '',
            password: ''
        }, 500);
    };
});

ubob.controller('SubscribeModalCtrl', function ($scope, $http, $element, ServletService, close) {
    $scope.username = '';
    $scope.password = '';

    $scope.createUser = function () {
        var validInput = true;

        $scope.usernameRequired = null;
        $scope.passwordRequired = null;
        $scope.errorMessage = null;

        if ($scope.username == '') {
            $scope.usernameRequired = "A username is required.";
            validInput = false;
        }

        if ($scope.password == '') {
            $scope.passwordRequired = "A password is required.";
            validInput = false;
        }

        if ($scope.password != $scope.repeatPassword) {
            $scope.passwordRequired = "The password and the repeated password don't match.";
            validInput = false;
        }

        if (validInput) {
            var success = ServletService.createUserAtServer($http, $scope.username, $scope.password);
            success.then(function (result) {
                if (result.data["success"] == "true") {
                    $element.modal('hide');
                    $scope.errorMessage = "";
                    close({
                        username: $scope.username,
                        password: $scope.password
                    }, 500);
                } else {
                    $scope.errorMessage = "User already exists!";
                }
            })
        }
    };

    $scope.cancel = function() {
        $element.modal('hide');

        close({
            username: '',
            password: ''
        }, 500);
    };
});