<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="loginModal modal fade">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" ng-click="cancel()" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label for="username" class="col-sm-2 control-label">Username</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="username" placeholder="Your Username" ng-model="username">
                            <span ng-show="usernameRequired">{{usernameRequired}}</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="password" class="col-sm-2 control-label">Password</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" id="password" placeholder="Password" ng-model="password">
                            <span ng-show="passwordRequired">{{passwordRequired}}</span>
                        </div>
                    </div>
                </form>

            </div>
            <div class="modal-footer">
                <span ng-show="errorMessage">{{errorMessage}}</span>
                <button type="button" ng-click="login()" class="btn btn-primary">Login</button>
                <button type="button" ng-click="cancel()" class="btn">Cancel</button>
            </div>
        </div>
    </div>
</div>