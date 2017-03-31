angular.module('AD122016')
    .controller('globalController', function($scope, AuthenticationService, $location, $rootScope) {
    	
    	
    	$scope.exit = function() {
    		console.log('Deslogando...');
    		AuthenticationService.ClearCredentials();
    		$location.path('/login');
    	};
    	
    	$scope.exibir = function() {
    		if($location.path() !== '/login') {
    			if($rootScope.globals.currentUser) {
    	    		$scope.username = $rootScope.globals.currentUser.username;
    	    		$scope.roles = $rootScope.globals.currentUser.roles;
    	    	};
    	    	
    	    	return true;
    		}
    	}
});