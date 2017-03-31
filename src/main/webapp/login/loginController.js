angular.module('AD122016').controller(
		'loginController',
		function($scope, $rootScope, $location, AuthenticationService) {
			AuthenticationService.ClearCredentials();

			$rootScope.exibirBotao = function(role) {
				console.log($rootScope.globals.currentUser.roles.indexOf(role));
				if ($rootScope.globals.currentUser.roles.indexOf(role) !== -1) {
					
					return true;
				} else {
					return false;
				}
			};

			$scope.login = function() {
				$scope.dataLoading = true;
				AuthenticationService.Login($scope.username, $scope.password,
						function(response) {
							AuthenticationService.SetCredentials(
									$scope.username, $scope.password,
									response['data']);
							$location.path('/');
						}, function(response) {
							$scope.error = response['status'];
							$scope.dataLoading = false;
						});
			};
		});