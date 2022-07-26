//----------------------------------------------------------------------------------------------------------------------
var isIE = document.all?true:false;
/* Variáveis para captura de coordenadas do mouse*/
if (!isIE) document.captureEvents(Event.MOUSEMOVE);

//----------------------------------------------------------------------------------------------------------------------
//Função que captura  dimensão da tela

var totalHeight; // variável que guardará a Altura da tela
var totalWidth; // variavél que guardará a Largura da tela
function getDimensao()
{
		if(isIE)
		{
			totalHeight = document.documentElement.clientHeight;
			totalWidth = document.documentElement.clientWidth;
		}
		else
		{
			totalHeight = window.innerHeight;
			totalWidth = window.innerWidth;
		}
}

function ajustaContainer()
{
	if(totalWidth)
	{
		var elContainer = document.getElementById('container');
		var elConteudo = document.getElementById('conteudo');
		var elInnerConteudo = document.getElementById('innerConteudo');
		if(totalWidth < 1016)
		{
			elContainer.style.width = '1000px';
		}
		else
		{
			elContainer.style.width = '';
		}
		if(elInnerConteudo)
		{
			if((elInnerConteudo.offsetHeight + 210) < document.documentElement.clientHeight)
			{
				elConteudo.style.height = (parseInt(totalHeight) - 190) + 'px';
			}
			else
			{
				elConteudo.style.height = '';
			}
		}
		
		
		
	}
}

function selectCheckBox(event)
{
	if(isIE)
	{
		elTarget = event.srcElement;
	}
	else
	{
		elTarget = event.target;
	}
	tdParent = elTarget.parentNode;
	trParent = tdParent.parentNode;
	if(elTarget.checked == true)
	{
		$(trParent).morph('background:#cfe2f5',{duration:0.3})
	}
	else
	{
		$(trParent).morph('background:#f4f4f4',{duration:0.3})
	}
}

function avoid()
{
}


function checkAll(el,field)
{
	if(el.checked == true)
	{
		for (i=0; i < field.length; i++)
     	{
     	field[i].checked = true;
     	tdParent = field[i].parentNode;
		trParent = tdParent.parentNode;
		$(trParent).morph('background:#cfe2f5',{duration:0.3})
		}
	}
	else
	{
		for (i=0; i < field.length; i++)
		{
     	field[i].checked = false;
     	tdParent = field[i].parentNode;
		trParent = tdParent.parentNode;
		$(trParent).morph('background:#f4f4f4',{duration:0.3})
		}
	}
}

function isShiftPressed(event)
{
	return (event.shiftKey == 1);
}

var lastCheck ='';
function multiCheck(event,field)
{
	//alert(isShiftPressed(event));
	var mark = false;
	if(isIE)
	{
		elTarget = event.srcElement;
	}
	else
	{
		elTarget = event.target;
	}
	if(isShiftPressed(event))
	{
		//field = event.target.parentNode;
		//field = elTarget.parentNode;
		if(!field)
		{
			field = elTarget.parentNode;
		}
		
		for (i=0; i < field.length; i++)
		{
			if(field[i] == lastCheck)
			{
				mark = false;
				for (n=0; n < field.length; n++)
				{
					if(field[n] == lastCheck)
					{
						mark = true;
					}
					else if(field[n] == elTarget)
					{
						mark = false;
					}
					else if(mark)
					{
						if(field[n].checked)
						{
							field[n].checked = false;
							tdParent = field[n].parentNode;
							trParent = tdParent.parentNode;
							$(trParent).morph('background:#f4f4f4',{duration:0.3})
						}
						else
						{
							field[n].checked = true;
							tdParent = field[n].parentNode;
							trParent = tdParent.parentNode;
							$(trParent).morph('background:#cfe2f5',{duration:0.3})
						}
					}
				}
				return;
			}
			if(field[i] == elTarget)
			{
				mark = false;
				for (n=0; n < field.length; n++)
				{
					if(field[n] == elTarget)
					{
						mark = true;
					}
					else if(field[n] == lastCheck)
					{
						mark = false;
					}
					else if(mark)
					{
						if(field[n].checked)
						{
							field[n].checked = false;
							tdParent = field[n].parentNode;
							trParent = tdParent.parentNode;
							$(trParent).morph('background:#f4f4f4',{duration:0.3})
						}
						else
						{
							field[n].checked = true;
							tdParent = field[n].parentNode;
							trParent = tdParent.parentNode;
							$(trParent).morph('background:#cfe2f5',{duration:0.3})
						}
					}
				}
				return;
			}		
		}
	}
	else
	{
		lastCheck = elTarget;
	}
		
	//alert(event)
}





