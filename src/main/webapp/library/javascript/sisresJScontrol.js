//----------------------------------------------------------------------------------------------------------------------
/*Conjunto de Fun��es que ser�o aplicadas aos eventos do Body do aplicativo*/
//----------------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------------
/*Fun��o controladora do evento rezise*/
function controlResize()
{
	getDimensao();
	ajustaContainer();
}

//----------------------------------------------------------------------------------------------------------------------
/*Regi�o que atribui as fun��es ao documento*/
//----------------------------------------------------------------------------------------------------------------------
var init = true;// liga os controladores de evento do documento

if(init)
{
	window.onresize = controlResize;
}