/**
 *  Confirmacao de exclusao de um contato
 * @author Pablo Zalem
 */

function confirmar(idcon){
	let resposta = confirm("Confirma a exclusao deste contato?")
	
	if(resposta === true)
		//alert(idcon)
		window.location.href = "delete?idcon=" + idcon	
}