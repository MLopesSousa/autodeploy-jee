angular.module('AD122016')
		.controller(
				'aplicacoesController',
				function($scope, dao, $timeout, $rootScope, $routeParams) {
					$scope.destroy = false;
					
					$scope.$on("$destroy", function(event) {
		            	$scope.destroy = true;
		            });
					
					if($routeParams.descricao) {
						$scope.search = $routeParams.descricao;
					};
					
					$scope.aplicacoes = [];
					$scope._novo = {};
					$scope.targets = [];
					$scope.mapaDeLinhas = {};
					$scope.aplicacaoSelecionada = {};
					$scope.aplicacaoEditar = {};
					$scope.exibirBotao = $rootScope.exibirBotao;
					
					$scope.timeouts = [];
					
					$scope.atualizarAplicacao = function(aplicacao) {
						var c = confirm("Confirmar?");

						if (c) {
							var DAO = dao.put("rs/aplicacoes/", aplicacao);
							DAO.then(function(data) {
								$scope.aplicacoes = data['data'];
							});
						}
					};
					
					$scope.selecionaAplicacaoEditar = function(data) {
						$scope.aplicacaoEditar = angular.copy(data);
			        };
			        
			        $scope.clonarObjeto = function(data) {
						$scope._novo = angular.copy(data);
						delete $scope._novo['id']; 
			        };
			        
					$scope.selecionaAplicacao = function(aplicacao) {
						$scope.aplicacaoSelecionada = aplicacao;
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

					var pegarAplicacoes = function() {
						var DAO = dao.listar("rs/aplicacoes/");
						DAO.then(function(data) {
							$scope.aplicacoes = data['data'];
						});
					};

					var poolingAplicacoes = function() {
						$scope.timeouts.push($timeout(function() {
							pegarAplicacoes();
							if(!$scope.destroy) {
								poolingAplicacoes();
							};
							
						}, 5000));
					};

					$scope.redeploy = function(deployId) {
						var c = confirm("Confirmar?");

						if (c) {
							var DAO = dao.post("rs/deploys/" + deployId
									+ "/redeploy/");
							DAO.then(function(data) {

							});
						}
					};
					
					$scope.removeAplicacao = function(aplicacaoId) {
						var c = confirm("Confirmar?");

						if (c) {
							var DAO = dao.remover("rs/aplicacoes/" + aplicacaoId);
							DAO.then(function(data) {
								$scope.aplicacoes = data['data'];
							});
						}
					};

					$scope.novaAplicacao = function() {
						$scope._novo = {
							"descricao" : "",
							"arquivo" : "",
							"estado" : false,
							"target" : [],
							"emails" : []
						};
					};

					$scope.criarAplicacao = function(data) {
						console.log('aqui');
						if (data instanceof Object) {
							var novo = dao.post("rs/aplicacoes", data);
							novo.then(function(data) {
								$scope.mensagensTemporarias.push({
									"titulo" : "INFO",
									"corpo" : "Aplicação "
											+ data['data']['descricao']
											+ " criada com sucesso !"
								});
								$scope.aplicacoes.push(data['data']);
								return true;

							}, function(erro) {
								$scope.mensagensTemporarias.push({
									"titulo" : "ERRO",
									"corpo" : "Erro ao criar aplicação: "
											+ data['descricao'] + " " + erro
											+ " "
								});
								return false;

							});
						}
					};

					listarTargets = function() {
			            var listar = dao.listar("rs/targets");
			            
			            listar.then(function(data) { 
			                    $scope.targets = data['data'];
			                }, function(data) {
			                    
			                }
			            );
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
			        
			        pegarAplicacoes();
			        listarTargets();
					poolingAplicacoes();
				});