angular.module('AD122016')
		.controller(
				'sasController',
				function($scope, dao, $timeout, $rootScope, $routeParams) {
					$scope.destroy = false;
					
					$scope.$on("$destroy", function(event) {
		            	$scope.destroy = true;
		            });
					
					if($routeParams.descricao) {
						$scope.search = $routeParams.descricao;
					};
				
					$scope.sas = [];
					$scope.mapaDeLinhas = {};
					$scope.objetoSelecionado = {};
					$scope.objetoEditar = {};
					$scope.exibirBotao = $rootScope.exibirBotao;
					
					$scope.timeouts = [];
					
					$scope.atualizar = function(target) {
						var c = confirm("Confirmar?");

						if (c) {
							var DAO = dao.put("rs/servidorDeAplicacao/", target);
							DAO.then(function(data) {
								$scope.sas = data['data'];
							});
						}
					};
					
					$scope.selecionaEditar = function(data) {
						$scope.objetoEditar = angular.copy(data);
			        };
			        
			        $scope.clonarObjeto = function(data) {
						$scope._novo = angular.copy(data);
						delete $scope._novo['id']; 
			        };
			        
					$scope.selecionaObjeto = function(objeto) {
						$scope.objetoSelecionado = objeto;
					};
					
					$scope.espandirLinha = function(id) {
						$scope.mapaDeLinhas[id] = !$scope.mapaDeLinhas[id];
					};

					$scope.estaLinhaEstaEspandida = function(id) {
						if ($scope.mapaDeLinhas[id] !== null) {
							return $scope.mapaDeLinhas[id];
						} else {
							return false;
						}
					};

					var pegarObjetos = function() {
						var DAO = dao.listar("rs/servidorDeAplicacao/");
						DAO.then(function(data) {
							$scope.sas = data['data'];
						});
					};

					var poolingObjetos = function() {
						$scope.timeouts.push($timeout(function() {
							pegarObjetos();
							if(!$scope.destroy) {
								poolingObjetos();
							};
							
						}, 5000));
					};
					
					$scope.remover = function(objetoId) {
						var c = confirm("Confirmar?");

						if (c) {
							var DAO = dao.remover("rs/servidorDeAplicacao/" + objetoId);
							DAO.then(function(data) {
								$scope.sas = data['data'];
							});
						}
					};

					$scope.criar = function() {
						return {
							"descricao" : "",
							"host" : "",
							"porta" : 0,
							"comando" : "",
							"estado" : true,
							"alias" : ""
						};
					};

					$scope.criarObjeto = function(data) {
						if (data instanceof Object) {
							var novo = dao.post("rs/servidorDeAplicacao", data);
							novo.then(function(data) {
								$scope.sas.push(data['data']);
								return true;

							}, function(erro) {
								return false;

							});
						}
					};
					
			        $scope.removerDoArray = function(arr, string) {
			            if(arr instanceof Array) {
			                for(var c = 0; c < arr.length; c++) {
			                    if(arr[c] == string) {
			                        arr.splice(c, 1);
			                    }
			                }
			            }
			        };
			        
			        $scope.removerNoArrayPorID = function(arr, id) {
			            if(arr instanceof Array && arr.length > 0) {
			                for(var c = 0; c < arr.length; c++) {
			                    if(arr[c]['id'] === id) {
			                        arr.splice(c, 1);
			                    }
			                }
			            }
			            
			            return false;
			        };
			        
			        pegarObjetos();
			        poolingObjetos();
				});