<%@ include file="common/header.jspf" %>
<%@ include file="common/navigation.jspf"%>
<div class="container">
	<div>
		<h1>Projetos</h1>
		<a type="button" class="btn btn-primary btn-md" href="/adicionar-projeto">Adicionar</a>
	</div>
	<br />
	<c:if test="${not empty projetos }">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>Nome</th>
					<th>Data Início</th>
					<th>Previsão</th>
					<th>Data Fim</th>
					<th>Orçamento (R$)</th>
					<th colspan="2">#</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${projetos}" var="projeto">
					<tr>
						<td>${projeto.nome }</td>
						<td><fmt:formatDate value="${projeto.dataInicio }" pattern="dd/MM/yyyy" /></td>
						<td>
							<fmt:formatDate value="${projeto.dataPrevisaoFim }" pattern="dd/MM/yyyy" />
						</td>
						<td><fmt:formatDate value="${projeto.dataFim }" pattern="dd/MM/yyyy" /></td>
						<td>
							<fmt:setLocale value = "pt_BR"/>
							<fmt:formatNumber value = "${projeto.orcamento }" type = "currency"/>
						</td>
						
						<td>
							<button type="button" class="btn btn-success"
									onclick="location.href='/editar-projeto?id=${projeto.idProjeto }'">Editar</button>
						</td>
						<td>
							<button type="button" class="btn btn-warning"
								onclick="location.href='/excluir-projeto?id=${projeto.idProjeto }'"
								<c:if test="${projeto.status eq 'INICIADO' or projeto.status eq 'EM_ANDAMENTO' or projeto.status eq 'ENCERRADO'  }">
									disabled
								</c:if>
								>Excluir</button>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>
	<c:if test="${empty projetos }">
		Ainda não há projetos cadastrados!
	</c:if>
</div>

<%@ include file="common/footer.jspf"%>