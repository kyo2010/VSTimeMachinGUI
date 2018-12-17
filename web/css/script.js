  var cashTime = Date.now();
  var URL = "";
  var requested = false;

  function updateTable(){
    if (requested==true) return;
    try {
      requested=true;
      var place = document.getElementById("place");
      //alert(place);
      place.innerHTML = loadHTML(URL+"time"+Date.now());
      cashTime = Date.now();
      var arr = place.getElementsByTagName('script')
      for (var n = 0; n < arr.length; n++){
        eval(arr[n].innerHTML);//run script inside div
      }
    }catch(err){}
    requested=false;
  };

  setInterval('checkCashe()', 1500);
  function checkCashe(){
    try {
      if ((cashTime+4000)<Date.now()){
        place.innerHTML = "";
      }
    }catch(err){}
  };

  function loadHTML(url){
     var req = null;
     if(window.XMLHttpRequest){           
       req = new XMLHttpRequest();
       if (req.overrideMimeType) {
         req.overrideMimeType('text/xml');
       }
       req.open("GET", url, false);
       req.send(null);
    }else if(window.ActiveXObject){
      req = new ActiveXObject("Microsoft.XMLHTTP");
      if(req){
        req.open("GET", url, false);
        req.send();
      }
    }
    if (req != null && req.responseText != undefined){
      return req.responseText;
    }else{
      return new String('');
    } 
  };
