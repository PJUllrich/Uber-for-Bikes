function Bike(id, location, available) {
    this.id = id;
    this.location = location;
    this.available = available;
}

function parseResult(UserService, result) {
    if (result.success == "true") {
        UserService.id = result.id;
        UserService.username = result.username;
        UserService.balance = result.balance;
        UserService.rented = JSON.parse(result.rented).filter(checkBikeObject);

        return true;
    }

    return false;
}

function checkBikeObject(bike) {
    return bike.available != null;
}

function bikeIndexInArray(bikes, id) {
    var index = 0;
    for (var i = 0; i < bikes.length; i++) {
        if (bikes[i].id == id) {
            index = i;
        }
    }
    return index;
}