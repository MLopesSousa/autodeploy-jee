angular.module('AD122016').controller('deploysController',
		function($scope, dao, $timeout, $routeParams) {
			$scope.destroy = false;
			
			$scope.deploys = [];
			$scope.mapaDeLinhas = {};
			
			if($routeParams.id) {
				$scope.searchId = $routeParams.id;
				$scope.mapaDeLinhas[$routeParams.id] = true;
				//$scope.deployId = $routeParams.id;
			};
			
			$scope.espandirLinha = function(id) {
				$scope.mapaDeLinhas[id] = !$scope.mapaDeLinhas[id];
			};
			
			$scope.estaLinhaEstaEspandida = function(id) {
				if($scope.mapaDeLinhas[id] !== null) {
					return $scope.mapaDeLinhas[id];
				} else {
					return false;
				}
			};
			
			var pegarDeploys = function() {
				var DAO = dao.listar("rs/deploys");
				DAO.then(function(data) {
					$scope.deploys = data['data'];
				});
			};
			
			var poolingDeploys = function() {
				$timeout(function() {
					pegarDeploys();
					
					if(!$scope.destroy) {
						poolingDeploys();
					}

				}, 4000);
			};

            $scope.$on("$destroy", function(event) {
            	console.log('destruindo ....');
            	$scope.destroy = true;
            });
            
			poolingDeploys();

		});