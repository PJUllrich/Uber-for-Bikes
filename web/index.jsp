<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html ng-app="ubobApp">
<head>
    <title> UBOB - Rent A Bike </title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="description" content="">
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no"/>

    <link rel="stylesheet" type="text/css" href="css/ubob.css">
    <link rel="stylesheet" href="css/modal.css">
    <link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/angular_material/1.1.0/angular-material.min.css">
</head>


<body ng-cloak layout="column">

<md-toolbar layout="row" class="md-toolbar-tools">
    <h1>Uber for Bikes</h1>
    <span flex></span>
    <div ng-controller="ButtonCtrl">
        <md-button id="registerButton" class="md-accent md-raised" ng-click="showRegisterModal()">Register</md-button>
    </div>
    <div ng-controller="ButtonCtrl">
        <md-button id="loginButton" class="md-accent md-raised" ng-click="showLoginModal()">Log In</md-button>
    </div>
</md-toolbar>

<div flex layout="row">
    <md-sidenav md-is-locked-open="true" class="md-whiteframe-4dp">
        <div ng-controller="UserCtrl">
            <md-list>
                <md-list-item>Username: {{service.username}}</md-list-item>
                <%--<md-list-item>Balance: {{service.balance | number:2}} EUR</md-list-item>--%>
                <md-list>
                    <md-list-item ng-repeat="bike in service.rented">
                        {{bike.location[0] | number: 3}} : {{bike.location[1] | number:3}}
                        <div ng-controller="ButtonCtrl">
                            <md-button class="md-accent md-raised" id="stopRentalButton"
                                       ng-click="changeBikeRental(bike, false)">
                                Stop Rental
                            </md-button>
                        </div>
                    </md-list-item>
                </md-list>
            </md-list>
        </div>
        <div ng-controller="ButtonCtrl">
            <md-button class="primary" id="remoteRandomBikeButton" ng-click="remoteAddRandomBike()">
                Remote: Add Random Bike
            </md-button>
        </div>
    </md-sidenav>
    <md-content flex ng-controller="MapCtrl">
        <ng-map ng-view center="[53.219, 6.567]" zoom="13" id="map">
            <info-window id="markerInfoWindow">
                <div ng-non-bindable>
                    <h3>Location: {{selectedBike.location[0] | number:3}} : {{selectedBike.location[1] | number:3}}</h3>
                    <h4>Available: {{selectedBike.available}}</h4>
                    <div ng-controller="ButtonCtrl">
                        <div ng-if="userIsLoggedIn()">
                            <div ng-if="selectedBike.available">
                                <md-button class="md-accent md-raised" id="rentBikeButton"
                                           ng-click="changeBikeRental(selectedBike, true)">Rent
                                </md-button>
                            </div>
                            <div ng-if="!selectedBike.available && bikeRentedByUser(selectedBike)">
                                <md-button class="md-accent md-raised" id="stopRentalBikeButton"
                                           ng-click="changeBikeRental(selectedBike, false)">Stop Rental
                                </md-button>
                            </div>
                        </div>
                        <div ng-if="!userIsLoggedIn()">
                            You're not logged in! Login or Register first!
                        </div>
                    </div>
                </div>
            </info-window>
            <marker ng-repeat="bike in bikeService.bikes" ng-if="bike.available"
                    icon={{normalImage}}
                    title="Title"
                    position="{{bike.location[0]}}, {{bike.location[1]}}"
                    z-index="1"
                    on-click="showBikeDetails(event, bike)"
            ></marker>
            <marker ng-repeat="rentedBike in userService.rented"
                    icon={{rentedImage}}
                    title="Title"
                    position="{{rentedBike.location[0]}}, {{rentedBike.location[1]}}"
                    z-index="1"
                    on-click="showBikeDetails(event, rentedBike)"
            ></marker>
        </ng-map>
    </md-content>
</div>
</body>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.2.4/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<script src="js/lib/angular.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/angular_material/1.1.0/angular-material.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.0rc1/angular-route.min.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.5.5/angular-animate.min.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.5.5/angular-aria.min.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.5.5/angular-messages.min.js"></script>
<script src="js/lib/angular-modal-service.js"></script>

<script src="js/lib/ui-bootstrap-tpls-2.1.4.min.js"></script>
<script src="https://maps.google.com/maps/api/js?libraries=placeses,visualization,drawing,geometry,places&key=AIzaSyBWdTO1PurWj3cFC48Ikx0TToxE2baph7k"></script>
<script src="https://rawgit.com/allenhwkim/angularjs-google-maps/master/build/scripts/ng-map.js"></script>

<script src="js/misc.js"></script>
<script src="js/ubob.js"></script>
</html>
