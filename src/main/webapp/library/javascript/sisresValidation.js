//----------------------------------------------------------------------------------------------------------------------
/*Biblioteca de Validação do SisRes*/
//----------------------------------------------------------------------------------------------------------------------

function selectValidation(id)
{
	elSelect = $(id);
	elLinha = 'linha_'+id;
	elMsg = 'msg_'+id;
	elMsgTxt = 'msgTxt_'+id;
	//alert(elSelect.options[elSelect.selectedIndex].value);
	if(elSelect.options[elSelect.selectedIndex].value == 'E')
	{
		//elLinha.morph('background:#080; color:#fff;');
		new Effect.Morph(elLinha, { style: 'background:#c40a0a; color: #fff;', duration: 0.2 });
		new Effect.Morph(elSelect.parentNode, { style: 'background:#c40a0a; color: #fff;', duration: 0.2 });
		if($(elMsg))
		{
			new Effect.Morph(elMsg, { style: 'background:#c40a0a; color: #fff;',duration: 0.2 });
			$(elMsgTxt).hide();
			$(elMsgTxt).style.color = '#fff';
			$(elMsgTxt).innerHTML='Opção inválida. Escolha outra por favor.';
			$(elMsgTxt).appear({duration: 0.2 });
			new Effect.Morph(elMsg, { style: 'background:#e4aaaa;', delay: 2 ,duration: 0.2 });
		}
		new Effect.Morph(elLinha, { style: 'background:#b74141;', delay: 2 ,duration: 0.2 });
		new Effect.Morph(elSelect.parentNode, { style: 'background:#b74141;', delay: 2 ,duration: 0.2 });
		
	}
	else
	{
		new Effect.Morph(elLinha, { style: 'background:#059805; color: #fff;', duration: 0.2 });
		new Effect.Morph(elSelect.parentNode, { style: 'background:#059805; color: #fff;', duration: 0.2 });
		if($(elMsg))
		{
			new Effect.Morph(elMsg, { style: 'background:#059805; color: #fff;', duration: 0.2 });
			$(elMsgTxt).hide();
			$(elMsgTxt).style.color = '#fff';
			$(elMsgTxt).innerHTML='Opção válida.';
			$(elMsgTxt).appear({duration: 0.2 });
			new Effect.Morph(elMsg, { style: 'background:#a0e2a0;', delay: 2 ,duration: 0.2 });
		}
		new Effect.Morph(elLinha, { style: 'background:#4dbc4d;', delay: 2,duration: 0.2 });
		new Effect.Morph(elSelect.parentNode, { style: 'background:#4dbc4d;', delay: 2 ,duration: 0.2 });
	}
}

function aplyMask(event)
{
	if(isIE)
	{
		elTarget = event.srcElement;
	}
	else
	{
		elTarget = event.target;
	}
	var masks = new Array({
		diamesano: {
	        format: '  /  /    ',
	        regex:  /\d/,
	        runMode: 'normal',
            regexFim: /^((((0?[1-9]|[12]\d|3[01])[\.\-\/](0?[13578]|1[02])[\.\-\/]((1[6-9]|[2-9]\d)?\d{2}))|((0?[1-9]|[12]\d|30)[\.\-\/](0?[13456789]|1[012])[\.\-\/]((1[6-9]|[2-9]\d)?\d{2}))|((0?[1-9]|1\d|2[0-8])[\.\-\/]0?2[\.\-\/]((1[6-9]|[2-9]\d)?\d{2}))|(29[\.\-\/]0?2[\.\-\/]((1[6-9]|[2-9]\d)?(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00)|00)))|(((0[1-9]|[12]\d|3[01])(0[13578]|1[02])((1[6-9]|[2-9]\d)?\d{2}))|((0[1-9]|[12]\d|30)(0[13456789]|1[012])((1[6-9]|[2-9]\d)?\d{2}))|((0[1-9]|1\d|2[0-8])02((1[6-9]|[2-9]\d)?\d{2}))|(2902((1[6-9]|[2-9]\d)?(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00)|00))))$/
	    },
	    mesano: {
	        format: '  /    ',
	        regex:  /\d/,
	        runMode: 'normal'
	    },
	    numero: {
	        format: '            ',
	        regex:  /\d/,
	        runMode: 'normal'
	    },
	    dinheiro: {
	        format: '                ',
	        regex:  /\d/,
	        runMode: 'dinheiro'
	    }
	});
	
	selectedMask = elTarget.getAttribute('validate');
	var getKey = function(e) {
    return window.event ? window.event.keyCode
           : e            ? e.which
           :                0;
  	}
  	
  	var isPrintable = function(key) {
      return ( key >= 32 && key < 232 );
   	}
   	var keyPicked = getKey(event);
   	if(isPrintable(keyPicked)){
   		var ch = String.fromCharCode(keyPicked);
   		var str = elTarget.value;
   		var pos = str.length+1;

		

   		if(masks[0][selectedMask].regex.test(ch) && pos <= masks[0][selectedMask].format.length)
   		{
   		 	if(masks[0][selectedMask].runMode == 'normal')
   			{
	   		 	if ( masks[0][selectedMask].format.charAt(pos -1) != ' ' ) 
	   			{
	            	str = elTarget.value + masks[0][selectedMask].format.charAt(pos -1);
	            }
	            elTarget.value = str;
	        }
	       	else if(masks[0][selectedMask].runMode == 'dinheiro')
	        {
	        	
	        	str2split = elTarget.value;
	        	strSplited = str2split.split(',');
	        	
	        	str = '';
	        	for(i=0;i<strSplited.length;i++)
	        	{
	        		str = str + strSplited[i];
	        	}
	        	
	        	str1 = str.substring(0, (str.length - 1));
	        	str2 = str.substr(str.length - 1);
	        	
	        	
	        	//alert(str1.substring(0,str1.length - 5))
	        	if(str1.length > 3)
	        	{
	        		
	        		str1splited = str1.split('.');
	        		str1 = '';
	        		for(s=0;s<str1splited.length;s++)
	        		{
	        			str1 = str1 + str1splited[s];
	        		}
	        		
	        		newstr = '';
					count = 0;
					for(n=str1.length;n>=0;n--)
					{
						if(count!=0 && count%3 == 0)
						{
							if(n!=0)
							{
								newstr = '.'+ str1.charAt(n)+newstr;
							}
							else
							{
								newstr = str1.charAt(n)+newstr;
							}
						}
						else
						{
							newstr = str1.charAt(n)+newstr;
						}
						count++;
					}
	        		
	        		str1 = newstr;
	        	}
	        	if(str.length>1)
	        	{
					elTarget.value = str1 + ',' + str2;
				}
				else
				{
					elTarget.value = str1 + str2;
				}
				
	        }
	        else
	        {
	        	Event.stop(event);
	        }	
   		}
   		else
   		{
   			Event.stop(event);
   		}
   	}
}