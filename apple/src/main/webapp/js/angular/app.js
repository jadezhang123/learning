/**
 * Created by Zhang Junwei on 2017/1/23 0023.
 */
angular.module('grow.header', []);
angular.module('grow.honor', []);

var growApp = angular.module('grow', ['grow.header', 'grow.honor']);

growApp.config(['$provide', function ($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: 'views/index.html',
            controller: 'HomeController'
        })
        .when('/', {
            templateUrl: 'views/index.html',
            controller: 'HomeController'
        })
        .when('/', {
            templateUrl: 'views/index.html',
            controller: 'HomeController'
        })
        .otherwise({
            redirectTo: '/'
        });
}]);