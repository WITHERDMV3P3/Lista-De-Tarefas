document.addEventListener("DOMContentLoaded", function(carregarTarefas) {

	fetch('/Lista-de-Tarefas/consultabd')
		.then(response => {
			if (!response.ok) {
				throw new Error('Erro na rede: ' + response.statusText);
			}
			return response.json();
		})
		.then(data => {
			const corpoTabela = document.getElementById('corpoTabela');
			data.forEach(item => {
				const novaLinha = document.createElement('tr');

				function formatarData(dataISO) {
				    if (dataISO && dataISO.includes('-')) {
				        const [ano, mes, dia] = dataISO.split('-');
				        return `${dia}/${mes}/${ano}`;
				    }
				    return dataISO; 
				}
				
				const colunas = [
					{ texto: item[0], classe: 'id_tarefa' },
					{ texto: item[1] },
					{ texto: item[2] },
					{ texto: formatarData(item[3]) },
				];

				colunas.forEach(coluna => {
					const td = document.createElement('td');
					td.textContent = coluna.texto;
					if (coluna.classe) {
						td.classList.add(coluna.classe);
					}
					novaLinha.appendChild(td);
				});
				
				const custo = parseFloat(item[2]);
				if (custo >= 1000) {
					novaLinha.classList.add('table-danger');
				}
				
				const colunaAcoes = document.createElement('td');

				const botaoExcluir = document.createElement('img');
				botaoExcluir.src = "/Lista-de-Tarefas/imagens/excluir.png";
				botaoExcluir.classList.add('btn-excluir');
				botaoExcluir.onclick = function(event) {
					excluirTarefa(event, item[0]);
				};

				const botaoEditar = document.createElement('img');
				botaoEditar.src = "/Lista-de-Tarefas/imagens/editar.png";
				botaoEditar.classList.add('btn-editar');
				botaoEditar.onclick = function(event) {
					editarTarefa(event, item[0], item[1], item[2], item[3]);
				};
				
				
				const botaoSubir = document.createElement('img');
				botaoSubir.src = "/Lista-de-Tarefas/imagens/subir.png";
				botaoSubir.classList.add('btn-subir');
				botaoSubir.onclick = function(){
					movertarefa(item[0],'up');
				};
				
				const botaoDescer = document.createElement('img');
				botaoDescer.src = "/Lista-de-Tarefas/imagens/descer.png";
				botaoDescer.classList.add('btn-descer');
				botaoDescer.onclick = function(){
					movertarefa(item[0],'down');
				};
				
				colunaAcoes.appendChild(botaoExcluir);
				colunaAcoes.appendChild(botaoEditar);
				colunaAcoes.appendChild(botaoSubir);
				colunaAcoes.appendChild(botaoDescer);

				novaLinha.appendChild(colunaAcoes);


				corpoTabela.appendChild(novaLinha);

			})
		})
		.catch(error => console.error('Erro:', error));
})

function excluirTarefa(event, idtarefa) {
	event.preventDefault();

	if (!confirm('Tem certeza que deseja excluir esta tarefa?')) {
		return;
	}

	fetch(`/Lista-de-Tarefas/excluireeditar?id_tarefa=${idtarefa}`, { method: 'DELETE' })
		.then(response => {
			if (!response.ok) {
				throw new Error('Erro ao excluir tarefa: ' + response.statusText);
			}
			alert('Tarefa excluída com sucesso!');
			location.reload();
		})
		.catch(error => console.error('Erro ao excluir a tarefa:', error));
}

function editarTarefa(event, idtarefa, nome, custo, data) {
	const modal = new bootstrap.Modal(document.getElementById('myModal2'));
	modal.show();


	document.getElementById('editar-nome-tarefa').value = nome;
	document.getElementById('editar-custo').value = custo;
	document.getElementById('editar-data').value = data;


	document.getElementById('botaoeditar').onclick = function(event) {
		event.preventDefault(); 

		const formData = new FormData();
		const novoNome = document.getElementById('editar-nome-tarefa').value;
		const novoCusto = document.getElementById('editar-custo').value;
		const novaData = document.getElementById('editar-data').value;


		if (novoNome) formData.append('editar-nome-tarefa', novoNome);
		if (novoCusto) formData.append('editar-custo', novoCusto);
		if (novaData) formData.append('editar-data', novaData);


		fetch(`/Lista-de-Tarefas/excluireeditar?id_tarefa=${idtarefa}`, {
			method: 'POST',
			body: formData
		})
			.then(response => {
				if (!response.ok) {
					alert("Nome existente, Tente novamente")
				}else if(response.ok){
				alert('Tarefa editada com sucesso!');
				location.reload(); 
				}
			})
			.catch(error => console.error('Erro ao editar a tarefa:', error));
	};
}

document.getElementById('salvar').addEventListener('click', function(){
	event.preventDefault(); 
	
	const formData = new FormData();
	formData.append('nome-tarefa', document.getElementById('nome-tarefa').value);
	formData.append('custo', document.getElementById('custo').value);
	formData.append('data', document.getElementById('data').value);

	fetch('/Lista-de-Tarefas/incluir', {
	    method: 'POST',
	    body: formData
	})
	.then(response => {
		if (!response.ok) {
			alert("Nome existente, Tente novamente")
		}else if(response.ok){
		alert('Tarefa adicionada com sucesso!');
		location.reload(); 
		}
	})
	.catch(error => console.error('Erro ao adicionar a tarefa:', error));
})


function movertarefa(idtarefa, direcao){
	
	fetch(`/Lista-de-Tarefas/moverTarefa?idtarefa=${idtarefa}&direcao=${direcao}`, {
		method: 'POST'
	})
	.then(response => {
		if(!response.ok){
			return alert('Não é possivel movimentar a primeira e última tarefa para cima ou para baixo');
		}
	})
	
	.then(() => {
		location.reload(); 
	})
}