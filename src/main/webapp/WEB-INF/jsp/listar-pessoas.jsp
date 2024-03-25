<%@ include file="common/header.jspf" %>
<%@ include file="common/navigation.jspf"%>
<div class="container">
	<div>
		<h1>Pessoas</h1>
		<a type="button" class="btn btn-primary btn-md" href="/adicionar-pessoa">Adicionar</a>
	</div>
	<br>
	<c:if test="${not empty pessoas }">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>Nome</th>
					<th>Fun��o</th>
					<th>Nascimento</th>
					<th colspan="2">#</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${pessoas}" var="pessoa">
					<tr>
						<td>${pessoa.nome }</td>
						<td>
							<c:if test="${pessoa.funcionario }">
								Funcion�rio
							</c:if>
							<c:if test="${pessoa.gerente }">
								Gerente
							</c:if>
						</td>
						<td>${pessoa.dataNascimento }</td>
						<td>
							<a type="button" class="btn btn-success"
									onclick="location.href='/editar-pessoa?id=${pessoa.idPessoa}'">Editar</a>
						</td>
						<td>	
							<a type="button" class="btn btn-warning"
								onclick="location.href='/excluir-pessoa?id=${pessoa.idPessoa}'">Excluir</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>
	<c:if test="${empty pessoas }">
		Ainda n�o h� pessoas cadastradas!
	</c:if>
</div>

<%@ include file="common/footer.jspf"%>