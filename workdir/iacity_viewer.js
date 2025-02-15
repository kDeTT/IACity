﻿// Variáveis para uso do Google Maps
var gmap_api_key;
var gmap_api_url;
var gmap_script;
var gmap_map;
var gmap_locations;
var gmap_polyline;

// Parâmetro com o caminho do arquivo de resultados
var resultFile = getParameterByName('resultfile');

// Inicializa a página
function init()
{
	// Definindo eventos
	document.getElementById('fileChooser').addEventListener('change', handleFileSelect, false);
	
	// Tratamento global de erros
	window.onerror = function(message, url, lineNumber) 
	{  
  		alert('Message: \n\n' + message + '\n\n URL: \n\n' + url + '\n\n Line Number: ' + lineNumber);
  		return true;
	}; 
	
	// Faz a leitura do arquivo contendo a chave da API do Google Maps
	gmap_api_key = readTextFile('GMAPS_API_KEY.txt');
	
	// Define a URL do javascript do Google Maps
	gmap_api_url = "https://maps.googleapis.com/maps/api/js?key=" + gmap_api_key + "&signed_in=true&callback=plotMap"
	
	// Adiciona o javascript do Google Maps na página
	gmap_script = document.createElement("script");
	gmap_script.type = "text/javascript";
	gmap_script.src = gmap_api_url;
	
	// Caso a página receba um arquivo de resultado como parâmetro
	// carregá-lo assim que o mapa for criado
	gmap_script.onload = function()
	{
      	if(resultFile != null)
		{
			parseIACityResultFile(readTextFile(resultFile));
			updateMap();
		}
    };
	
	document.body.appendChild(gmap_script);
}

// Inicializa o mapa
function plotMap()
{
	if(gmap_map == null)
	{
		gmap_map = new google.maps.Map(document.getElementById('gmap'), 
		{
			// Define os controles que serão permitidos
			zoomControl: true,
  			mapTypeControl: false,
  			scaleControl: false,
  			streetViewControl: false,
  			rotateControl: false,
		
			// Define o tipo de mapa
			mapTypeId: google.maps.MapTypeId.ROADMAP,
		
			// Zoom e centro do mapa
    		zoom: 7,
    		center: {lat: -16.6954999, lng: -49.4443554}
  		});
	}
	
	if(gmap_polyline == null)
	{
		gmap_polyline = new google.maps.Polyline(
		{
    		path: gmap_locations,
    		geodesic: true,
    		strokeColor: '#FF0000',
    		strokeOpacity: 1.0,
    		strokeWeight: 2
  		});
	}
	
  	gmap_polyline.setMap(gmap_map);
}

/*
 *	Atualiza o caminho desenhado no mapa
 */
function updateMap()
{
	gmap_map.setCenter(gmap_locations[0]);
	gmap_polyline.setPath(gmap_locations);
}

/*
 *	Carrega um caminho no mapa, quando um novo arquivo for
 *	selecionado pelo usuário
 */
function handleFileSelect(evt)
{
	var selectedFile = evt.target.files;
	parseIACityResultFile(readTextFile('result/' + selectedFile[0].name));
	updateMap();
}

/*
 *	Faz o parser do arquivo de resultado lido
 */
function parseIACityResultFile(content)
{
	var line;
	var lineList = content.split("\n");
	var attrList;
	var again = true;
	
	gmap_locations = new Array();
	
	// Faz a leitura de todo o arquivo
	for(i = 0; ((i < lineList.length) && again); i++)
	{
		// Remove espaços em branco
		line = lineList[i].replace(/\s/g, '');
		
		// Se é o início da solução
		if(line.toUpperCase() == "BEGIN_SOLUTION")
		{
			var j = (i + 1);
			line = lineList[j];
			
			// Enquanto não chegar ao fim da solução
			while(line.toUpperCase() != "END_SOLUTION")
			{
				// Remove espaços e os caracteres de início e fim
				line = line.replace(/\s/g, '');
				line = line.replace('<', '');
				line = line.replace('>', '');
				
				// Separa os atributos
				attrList = line.split(',');
				
				if(attrList.length == 3)
				{	
					// Adiciona a coordenada para ser exibida no mapa
					gmap_locations.push({lat: parseFloat(attrList[1]), lng: parseFloat(attrList[2])});
				}
				
				j++;
				line = lineList[j];
			}
			
			// Encerra a leitura
			again = false;
		}
	}
}

/*
 *	Obtém um parâmetro da URL
 */
function getParameterByName(name, url) 
{
    if (!url)
	{
		url = window.location.href;
	}
	
    name = name.replace(/[\[\]]/g, "\\$&");
	
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);

    if (!results) 
	{
		return null;
	}
	
    if (!results[2])
	{
		return '';
	}
	
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

/*
 *	Lê um arquivo de texto.
 *	filePath deve ser informado no padrão: file:///C:/your/path/to/file.txt
 */
function readTextFile(filePath)
{
	var text;
    var rawFile = new XMLHttpRequest();
	
    rawFile.open("GET", filePath, false);
	
    rawFile.onreadystatechange = function ()
    {
        if(rawFile.readyState === 4)
        {
            if(rawFile.status === 200 || rawFile.status == 0)
            {
                text = rawFile.responseText;
            }
        }
    }
	
    rawFile.send(null);
	
	return text;
}