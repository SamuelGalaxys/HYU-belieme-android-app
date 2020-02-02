<script type="text/javascript">
var _submit = true;
var _complete = false;
var contextPath = '';
$( document ).ready(function() {
	var _t = null;
	(function getToken(t) {
		if (_t == '' || _t != t) {
			_curtimecheck = 0;
			fnGetPublicToken(contextPath + '/oauth/public_token.json?t=' + t);
			_t = t;
		}
	})('mobile');

    $('button#login_btn').click(function(e) {
    	doMemeberLogin();
    });

    $('input#upw').keydown(function(e) {
    	if (e.keyCode == 13) {
    		e.preventDefault();
			e.stopPropagation();

			doMemeberLogin();
    	}
    });
    $('#uid').focus();

	function doMemeberLogin() {
		if (_submit == true && _complete == false) {
			_submit = false;

			var $uid = $('#uid');
		    var $upw = $('#upw');

		    if ($uid.val() == '' || $uid.val() == '아이디를 입력하세요') {
		        alert("아이디를 입력하세요.");
		        $uid.focus();
		        return;
		    }
		    if ($upw.val() == '' || $upw.val() == '비밀번호를 입력하세요') {
		        alert("비밀번호를 입력하세요.");
		        $upw.focus();
		        return;
		    }

		    if (typeof _public_key == 'undefined' || _public_key == '') {
				if (_token_req_cnt > 0) {
					alert("서버에 암호화를 위한 데이터요청중입니다. 잠시만 기다려주세요.");
					return;
				}
			}

		    var protocol = window.location.protocol;
			var host = window.location.host;
			var port = window.location.port == '' ? '' : ':' + window.location.port;
			var domain = protocol + '//' + host + port + contextPath;

		    $.ajax({
		        type: "post",
		        url: domain + "/oauth/login_submit.json",
		        cache: false,
		        async: false,
		        data: {
					_userId: fnRSAEnc($uid.val()),
					_password: fnRSAEnc($upw.val()),
					identck: _public_key_nm,
					sinbun: ''
				},
		        dataType: "json",
		        success: function(data) {
		        	console.log(data.code);
		        	console.log(data.url);
		        	if (data.code == "200") {
		        		_complete = true;
		        		location.href = contextPath + data.url;
		        	} else if (data.code == "503") {
		        		// #6451 비밀번호 5회 오류는 포털 URL로 이동.
		        		_submit = true;
		        		alert(data.msg);
		        		location.replace(data.url);
		        	} else if (data.code == "504") {
		        		_complete = true;
		        		alert(data.msg);
		        		location.href = contextPath + data.url;
		        	} else {
		        		alert(data.msg);
		        	}
		        	_submit = true;
		        },
		        error: function(e) {
		            alert(e.responseText);
		        }
		    });
		}
	}
});


</script>