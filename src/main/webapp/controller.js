var app = angular.module('app',['ngAnimate']);
app.factory("sessionFactory", SessionFactory);
app.factory("downloadRequestFactory", DownloadRequestFactory);

app.controller('sessionCtrl', ['$scope','$interval','sessionFactory','downloadRequestFactory'
, function ($scope,$interval, SessionFactory,DownloadRequestFactory) {
    // Do something with myService
    console.log("sge");
    $scope.records = {}
    
    $scope.downloadClick = function(){
        var type = "audio"
        if ($scope.type != null){
            type = $scope.type;
        }
        
        console.log("download " + $scope.link);
        console.log("download " + type);
        var properties = {
            mediaType : type
        }

        if ($scope.link != null){
            var req = DownloadRequestFactory.get($scope.link,properties,SessionFactory.get());
            console.log(req);
            DownloadRequestFactory.initDownload(req).then(function(response){
                console.log(response);
                $scope.records[response.data.id] = response.data;    
            });
        } else {

        }
    }


    var updateDownloads = function(){
        console.log("update");
        DownloadRequestFactory.getCurrentDownloads(SessionFactory.get()).then (function(response){

            var list = response.data.logObserversDTOList;

            if ($scope.downloads == null){
                $scope.downloads = list;

            } else {
                var finished = [];
                for (j = 0; j<$scope.downloads.length; j++ ){
                    if ($scope.downloads[j].state === 'finished'){
                        finished.push(j);
                    }
                }

                for (i = 0; i< finished.length; i++){
                    $scope.downloads.splice(finished[i],1);
                }

                for (i=0; i<list.length; i++){
                    var e = -1;
                    for (j = 0; j<$scope.downloads.length; j++ ){
                        if ($scope.downloads[j].recordUUID === list[i].recordUUID){
                            e = j;
                        }
                    }

                    if (e < 0){
                        $scope.downloads.push(list[i]);
                    } else {
                        $scope.downloads[e].state = list[i].state;
                        $scope.downloads[e].title = list[i].title;
                        $scope.downloads[e].percentage = list[i].percentage;
                    }

                }

                for (j = 0; j<$scope.downloads.length; j++ ){
                    var e = -1;
                    for (i=0; i<list.length; i++){
                        if ($scope.downloads[j].recordUUID === list[i].recordUUID){
                            e = j;
                        }
                    }
                    if (e < 0){
                        $scope.downloads[j].percentage = 100;
                        $scope.downloads[j].state = 'finished';
                    }
                }

            }
            console.log($scope.downloads);
        });
       }
    
    $scope.useInterval = function() {
        //Show current seconds value 5 times after every 1000 ms
        $interval(updateDownloads, 1000);
  
      };

    $scope.useInterval();
    $scope.enableDownload = true;
  
    
    SessionFactory.get().then(function (response){
        var token = SessionFactory.get();
        var result = token == null || token == 'error';
        
        if (!result){
            $scope.enableDownload = result;
            var $loadingIcon = $('#loadingIcon');
            $loadingIcon.hide('slow', function(){ $loadingIcon.remove(); });
        } else {
            error();
        }
    });
    
}]);
function SessionFactory($http) {

   var _token = null;
    var request = {
        method: 'GET',
        url: 'http://localhost:8080/YT/rest/session/create',
        headers: {
         'Content-Type': 'application/json'
        }
    };

    function getSession(req) { 
        return http = $http(req).then(function successCallback(response){
            _token = response.data.token;
        },function failed(response){
            _token = 'error';
        });
    };
    return {
        get : function(){
            if (_token == null){
                _started = true;
                return getSession(request);
            } else {
                return _token;
            }
        }
    }
}

function helloWorld() {
    return 'Hello world!';
}
 function error(){
        var $errorBand = $('#errorBand');
        var $errorMessage = $('#errorMessage');
        $errorMessage.html("<strong>Error</strong> Could not reach the server. Please refresh to try again.");
        $errorBand.fadeTo(1000,1);
      }

function DownloadRequestFactory($http){
    var preparedRequest = {
        method: 'POST',
        url: 'http://localhost:8080/YT/rest/download/initialize',
        headers: {
         'Content-Type': 'application/json'
        }
        //data needed
    };

    function initDownload(req){

        return http = $http(req)

    }

    return {
        get : function(link, properties, session){
            if (link == null){
                throw "link not defined";
            }
            if (session == null){
                throw "session not defined";
            }
            if (properties == null || properties.mediaType == null){
                throw "properties not defined";
            }

            var req = {
                method: preparedRequest.method,
                url : preparedRequest.url,
                headers : preparedRequest.headers
            }
            req.headers['X-session-token'] = session;
            var data = {}
            data['mediaType'] = properties.mediaType;
            data['url'] = link;
            req.data = data;
            return req;
        },
        initDownload : function(req){
            if (req == null){
                throw "request not defined";
            }
            return initDownload(req);
        },
        getCurrentDownloads : function(session){
            var req = {
                method: 'GET',
                url: 'http://localhost:8080/YT/rest/download/current/all',
                headers: {
                    'X-session-token': session
                }
            }
            return http = $http(req);

        }

    }
}

function DownloadArray(){

    this._downloadArray = [];

    this.setAsScope = function ($scope){
        this._downloadSet = $scope.records;
    }

    this.updateState = function(downloadInfoService){
        for (var i = 0; i< this._downloadArray.size();i++){
            downloadInfoService.update(temp);
        }
    }

    this.delete = function(id){
        for (var i = 0; i< this._downloadArray.length;i++){
            var record = this._downloadArray[i];
            if (record.id === id){
                this._downloadArray.splice(i,1);
            }
        }
    }

    this.push = function(record){
        var canBePushed = true;
        for (var i = 0; i< this._downloadArray.length;i++){
            var record = this._downloadArray[i];
            if (record.id == id){
                canBePushed = false;
            }
        }

        if (canBePushed){
            this._downloadArray.push(record);
        }
    }

}