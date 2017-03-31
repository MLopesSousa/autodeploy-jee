angular.module('AD122016',['ngRoute', 'ngResource', 'ngCookies'])
    .config(function($routeProvider) {
        $routeProvider
            .when('/aplicacoes', {
                templateUrl: 'aplicacoes/index.html',
      		    controller: 'aplicacoesController'
            }).when('/aplicacoes/:descricao', {
                templateUrl: 'aplicacoes/index.html',
      		    controller: 'aplicacoesController'
            }).when('/targets', {
                templateUrl: 'targets/index.html',
      		    controller: 'targetsController'
            }).when('/targets/:descricao', {
                templateUrl: 'targets/index.html',
      		    controller: 'targetsController'
            }).when('/painel', {
                templateUrl: 'painel/index.html',
      		    controller: 'painelController'
            }).when('/deploys', {
            	templateUrl: 'deploys/index.html',
      		    controller: 'deploysController'
            }).when('/', {
            	templateUrl: 'deploys/index.html',
      		    controller: 'deploysController'
            }).when('/login', {
                controller: 'loginController',
                templateUrl: 'login/index.html'
            }).when('/deploys/:id', {
            	templateUrl: 'deploys/index.html',
      		    controller: 'deploysController'
            }).when('/sas', {
            	templateUrl: 'servidoresDeAplicacao/index.html',
      		    controller: 'sasController'
            }).when('/sas/:descricao', {
            	templateUrl: 'servidoresDeAplicacao/index.html',
      		    controller: 'sasController'
            });
	})
	
	.run(function ($rootScope, $location, $cookieStore, $http) {
        // keep user logged in after page refresh
        $rootScope.globals = $cookieStore.get('globals') || {};
        if ($rootScope.globals.currentUser) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata; // jshint ignore:line
        }
  
        $rootScope.$on('$locationChangeStart', function (event, next, current) {
            // redirect to login page if not logged in
            if (!$location.path().contains('/deploys/') && $location.path() !== '/login' && !$rootScope.globals.currentUser) {
                $location.path('/login');
            }
        });
    });
