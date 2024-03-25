<%@ include file="common/header.jspf" %>
<%@ include file="common/navigation.jspf"%>
<div class="container">
	<div>
		<h1>Projetos</h1>
		<a type="button" class="btn btn-primary btn-md" href="/adicionar-projeto">Adicionar</a>
	</div>
	<br />
	<div class="row">
		<span>Tem certeza que deseja excluir o projeto ${projeto.nome }?</span>
		<div class="col-sm">
			<button type="button" class="btn btn-danger"
									onclick="location.href='/confirma-excluir-projeto?id=${projeto.idProjeto }'">Excluir</button>
		</div>
		<div class="col-sm">
			<button type="button" class="btn btn-success"
									onclick="location.href='/listar-projetos'">Cancelar</button>
		</div>
	</div>
</div>