angular.module('AD122016')
    .factory('dao', function($http) {
        return { listar: function(url) {   
                        return $http.get(url); 
                },
                get: function(url) {   
                    return $http.get(url); 
                },
                 post: function(url, body) {
                        if(body) {
                            return $http.post(url, body);
                        } else {
                            return $http.post(url);
                        }
                },
                put: function(url, obj) {
                    return $http.put(url, obj);
                 },
                remover: function(url) {
                    return $http.delete(url);
                }
            }
    })