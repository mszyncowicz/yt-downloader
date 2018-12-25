describe('Hello world', () => {

    let expected = "";
  
    beforeEach(() => {
      expected = "Hello world!";
    });
  
    afterEach(() => {
      expected = "";
    });
  
    it('says hello', () => {
      expect(helloWorld())
          .toEqual(expected);
    });
  });

  describe('sessionFactory',function(){

    var sessionFactory;

    beforeEach(() => {
      module('app');
      
      inject(function($injector){
        sessionFactory = $injector.get('sessionFactory');
      });

    });
  
    it('has to be defined',() => {
      var get = sessionFactory.get();
      console.log(get);
      expect(get).toBeDefined();
    })
  });

  describe('DownloadRequestFactory',function(){
    var downloadRequestFactory,$httpBackend;
    var properties = {};
    var test;
    const link = "youtube link";

    beforeEach(() => {
      module('app');
      
      properties.mediaType = "video";

      inject(function($injector){
        $httpBackend = $injector.get('$httpBackend');
        downloadRequestFactory = $injector.get('downloadRequestFactory');
        test = downloadRequestFactory.get(link,properties,'bla');
    
      });
    });

    it('null check session', () => {
      expect(() => {downloadRequestFactory.get(link,properties,null)}).toThrow('session not defined');
    });

    it('null check properties', () => {
      expect(() => {downloadRequestFactory.get(link,null,'ll')}).toThrow('properties not defined');
    });

    it('null check link', () => {
      expect(() => {downloadRequestFactory.get(null,properties,'ll')}).toThrow('link not defined');
    });

    it('null check mediaType', () => {
      var props = {};
      expect(() => {downloadRequestFactory.get(link,props,'ll')}).toThrow('properties not defined');
    });

    it('get has to be defined',() => {
      console.log(test);
      expect(test).toBeDefined();
    })

    it('get has to have data defined', () => {
      expect(test.data).toBeDefined();
    })
    
    it('data has to have mediaType defined', () => {
      expect(test.data['mediaType']).toBeDefined();
    })

    it('data has to have url defined', () => {
      expect(test.data['url']).toBeDefined();
    })

    it('mediaType has to equal properties.mediaType', () => {
      expect(test.data['mediaType']).toEqual(properties.mediaType);
    })

    it('url has to equal youtube link', () => {
      expect(test.data['url']).toEqual(link);
    })

    it('should get record', () => {
    var properties = {
      mediaType : "audio",
    }
    var url = 'http://localhost:8080/rest/download/initialize';
    var response =  { id: 'some uuid' };
    $httpBackend
      .when('POST', 'http://localhost:8080/rest/download/initialize')
      .respond(200, response);

     // console.log(r);
     $httpBackend.expectPOST(url).respond(response);
      downloadRequestFactory.initDownload(downloadRequestFactory.get(url,properties,"bla")).then(function(resp){
        console.log(resp);
        expect(resp.data).toEqual(response);
      });
      
      $httpBackend.flush();

    })
  });

