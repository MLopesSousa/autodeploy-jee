angular.module('AD122016').controller(
		'painelController',
		function($scope, dao, $timeout) {
			$scope.destroy = false;

			$scope.$on("$destroy", function(event) {
				console.log('destruindo ....');
				$scope.destroy = true;
			});

			$scope.configuracoes = [];
			$scope.workers = [];
			$scope.filas = [];
			$scope.deploys = [];
			$scope.aplicacoes = [];
			$scope.targets = [];

			$scope.eBoolean = function(string) {
				if (typeof string === 'boolean') {
					return true;
				} else {
					return false;
				}
			};

			var pegarConfiguracoes = function() {
				var DAO = dao.listar("rs/controle/configuracoes/");
				DAO.then(function(data) {
					$scope.configuracoes = data['data'];
				});
			};

			var pegarWorkers = function() {
				var DAO = dao.listar("rs/workers/");
				DAO.then(function(data) {
					$scope.workers = data['data'];
				});
			};

			var pegarFilas = function() {
				var DAO = dao.listar("rs/filas/all");
				DAO.then(function(data) {
					$scope.filas = data['data'];
				});
			};

			var pegarDeploys = function() {
				var DAO = dao.listar("rs/deploys");
				DAO.then(function(data) {
					$scope.deploys = data['data'];
				});
			};

			var pegarAplicacoes = function() {
				var DAO = dao.get("rs/aplicacoes");
				DAO.then(function(data) {
					$scope.aplicacoes = data['data'];
				});
			};

			var pegarTargets = function() {
				var DAO = dao.get("rs/targets");
				DAO.then(function(data) {
					$scope.targets = data['data'];
				});
			};

			$scope.removerWorker = function(worker) {
				if (worker != null) {
					var DAO = dao.listar("rs/workers/remover/" + worker);
					DAO.then(function(data) {

					});
				}
			};

			$scope.alterarValorDaPropriedade = function(chave, valor) {
				if (chave != null && valor != null) {
					var DAO = dao.post("rs/controle/configuracoes/" + chave
							+ "/" + valor);
					DAO.then(function(data) {
					});
				}
			};

			$scope.alterarValorDaPropriedadeString = function(chave, valor) {
				if (chave != null && valor != null) {
					var DAO = dao.post("rs/controle/configuracoes/string/"
							+ chave + "/" + valor);
					DAO.then(function(data) {
					});
				}
			};

			var poolingWorkers = function() {
				$timeout(function() {
					pegarWorkers();

					if (!$scope.destroy) {
						poolingWorkers();
					}
					;

				}, 1000);
			};

			var poolingFilas = function() {
				$timeout(function() {
					pegarFilas();

					if (!$scope.destroy) {
						poolingFilas();
					}
					;

				}, 1000);
			};

			var poolingDeploys = function() {
				$timeout(function() {
					pegarDeploys();

					if (!$scope.destroy) {
						poolingDeploys();
					}
					;
				}, 1000);
			};

			var poolingConfiguracoes = function() {
				$timeout(function() {
					pegarConfiguracoes();

					if (!$scope.destroy) {
						poolingConfiguracoes();
					}
					;

				}, 1000);
			};

			var poolingAplicacoes = function() {
				$timeout(function() {
					pegarAplicacoes();
					if (!$scope.destroy) {
						poolingAplicacoes();
					}
					;

				}, 1000);
			};

			var poolingTargets = function() {
				$timeout(function() {
					pegarTargets();
					if (!$scope.destroy) {
						poolingTargets();
					}
					;

				}, 1000);
			};

			poolingConfiguracoes();
			poolingWorkers();
			poolingFilas();
			poolingDeploys();
			poolingAplicacoes();
			poolingTargets();

		});