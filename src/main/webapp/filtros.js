angular.module('AD122016')
    .filter('formatarData', function() {
        return function(input){
            return new Date(input);
        }
    }).filter('removerAspasDuplas', function() {
        return function(input) {
            return JSON.stringify(input).replace(/"/g,'').replace(/T/,' ').split(".")[0];
        }
    }).filter('verificaFalhaOuSucessoNaString', function($sce) {
        return function(input) {
        	if(input.includes("SUCESSO")) {
    			return $sce.trustAsHtml('<span style="color: green">' + input + '</span>');
    		} else if(input.includes("FALHA")) {
    			return $sce.trustAsHtml('<span style="color: red">' + input + '</span>');
    		} else {
    			return $sce.trustAsHtml('<span style="color: green">' + input + '</span>');
    		}
        }
    }).filter('estadoDaAplicacao', function() {
        return function(input) {
            if(input) { 
                return "habilidato"; 
            } else { 
                return "desabilitado"; 
            }
        }
    }).filter('filtroEstado', function($sce) {
    	return function(input) {
    		if(input == "ERRO") {
    			return $sce.trustAsHtml('<span class="label label-danger">' + input + '</span>');
    		} else if (input === true) {
    			return $sce.trustAsHtml('<span class="label label-success">' + input + '</span>');
    		} else if (input === false) {
    			return $sce.trustAsHtml('<span class="label label-danger">' + input + '</span>');
    		} else if (input == "FINALIZADO") {
    			return $sce.trustAsHtml('<span class="label label-success">' + input + '</span>');
    		} else {
    			return $sce.trustAsHtml('<span class="label label-warning">' + input + '</span>');
    		}
    	}
    });