/*Importiamo jquery*/
document.write('<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>');

/*funzione per caricare l'immagine captcha*/
$(document).ready(function() {
	loadHiddenFormInput();
	loadTable();
	loadCaptchaImage();	
});

/*function loadCaptcha() {
	loadHiddenFormInput();
	loadTable();
	loadCaptchaImage();	
}*/

function loadHiddenFormInput() {
	$('<input>').attr({type: 'hidden', id: 'aria-captcha-response', name: 'response', value: ''}).appendTo('form');
	$('<input>').attr({type: 'hidden', id: 'aria-captcha-req', name: 'reqCode', value: ''}).appendTo('form');
	$('<input>').attr({type: 'hidden', id: 'aria-captcha-session-req', name: 'sessionReqCode', value: ''}).appendTo('form');
}

function loadTable() {
	$("#aria-custom-captcha").append('<table id="aria-custom-table"> '+
						'<tr>'+
							'<td><img src="/resources/images/arrow_up_left.png"  height="42" width="52"  id="UP-LEFT"></td>'+
							'<td><img src="/resources/images/arrow_up.png" height="42" width="52"        id="UP"></td>'+
							'<td><img src="/resources/images/arrow_up_right.png"  height="42" width="52" id="UP-RIGHT"></td>'+
						'</tr>'+
						'<tr>'+
							'<td><img  src="/resources/images/arrow_left.png" height="42" width="52"     id="LEFT"></td>'+
							'<td align="center"><img class="resize_fit_center" id="captcha_image" src=""  height="42" width="52" />'+
							'<td><img src="/resources/images/arrow_right.png" height="42" width="52"     id="RIGHT"></td>'+
						'</tr>'+
						'<tr>'+
							'<td><img src="/resources/images/arrow_down_left.png"  height="42" width="52" id="DOWN-LEFT"></td>'+
							'<td><img src="/resources/images/arrow_down.png" height="42" width="52"       id="DOWN"></td>'+
							'<td><img src="/resources/images/arrow_down_right.png" height="42" width="52" id="DOWN-RIGHT"></td>'+
						'</tr>'+
					'</table>');
	enableCaptchaButton();
}
 
function enableCaptchaButton() {
	var arrows = ["#UP-LEFT", "#UP", "#UP-RIGHT", "#LEFT", "#RIGHT", "#DOWN-LEFT", "#DOWN", "#DOWN-RIGHT"];
		
	for (i=0; i<arrows.length; i++) {
		$(arrows[i]).click(function() {
			this.disabled = true;
		    $('#aria-captcha-response').val(this.id);
		    validateCaptcha();
		});
	}
}

function loadCaptchaImage() {
		$('#aria-captcha-response').val("");
	    $.ajax({
	        url: serverContext +"captcha",
	        type: "GET",
        	dataType: "json",
        	/* contentType: "application/json; charset=utf-8", */
       		data:{
        		sessionReqCode: $('#aria-captcha-session-req').val()
        	}
	    }).then(function(data) {
	    	var waitingTime = data.waitingTime;
	    	if (waitingTime === null || waitingTime.length === 0 || waitingTime === undefined) {
		    	var src = "data:image/png;charset=utf-8;base64,"+data.image;
		    	var reqCode = data.reqCode;
		    	var sessionReqCode = data.sessionReqCode;
		    	$('#captcha_image').attr('src', src);
		  		
		    	$('#aria-captcha-session-req').val(sessionReqCode);
		    	$('#aria-captcha-req').val(reqCode);
	    	} else {
	    		$(function() {showWaitingTime(data.waitingTime)});
	    	}
	    		
	    });
}

var x;
var countDownDate;

function showWaitingTime(waitingTime) {
	/*Set the date we're counting down to*/
	countDownDate = waitingTime;

	// Update the count down every 1 second
	x = setInterval(countdown, 1000);
}

function countdown() {	
	// Get today's date and time
	var now = new Date().getTime();
	
	// Find the distance between now and the count down date
	var distance = countDownDate - now;
	
	// Time calculations for days, hours, minutes and seconds
	var days = Math.floor(distance / (1000 * 60 * 60 * 24));
	var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
	var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
	var seconds = Math.floor((distance % (1000 * 60)) / 1000);
	
	$("#aria-custom-table").remove();
	
	// Output the result in an element with id="aria-custom-captcha"
	var message = "Il Captcha Aria non e' stato validato correttamente. Riprova" + " tra "+ minutes + "m " + seconds + "s ";
	$("#aria-captcha-error").show().html(message);  
	
	  // If the count down is over, write some text 
	  if (distance < 0) {
	    console.log(x);
	    clearInterval(x);
	   	$("#aria-captcha-error").html("").hide();
	    loadTable();	    
	    loadCaptchaImage();
	  }
	}


function validateCaptcha() {
    $.ajax({
        url: serverContext +"captcha/validate",
        type: "POST",
        dataType: "json",
        /* contentType: "application/json; charset=utf-8", */
        data:{
        	reqCode: $('#aria-captcha-req').val(), 
        	response: $('#aria-captcha-response').val()
        }
    }).done(function(data) {
    	var src = "/resources/images/green_tick.png";
	    $('#captcha_image').attr('src', src);
    	$("#UP-LEFT").remove();
	    $("#UP").remove();
	    $("#UP-RIGHT").remove();
	    $("#LEFT").remove();
	    $("#RIGHT").remove();
	    $("#DOWN-LEFT").remove();
	    $("#DOWN").remove();
	    $("#DOWN-RIGHT").remove();
	    /* $('#captcha_image').remove(); */
	    $("#aria-captcha-error").remove(); 	    
    }).fail(function(data) {
      if(data.responseJSON.error == "InvalidReCaptcha"){
          $("#aria-captcha-error").show().html(data.responseJSON.message); 
          $('#aria-captcha-response').val('') ;
          loadCaptchaImage();
      }
    });
}
