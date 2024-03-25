<%@ include file="common/header.jspf"%>
<%@ include file="common/navigation.jspf"%>
<div class="container">
	<div class="row">
		<div class="col-md-6 col-md-offset-3 ">
			<div class="panel panel-primary">
				<div class="panel-heading">Editar Pessoa</div>
				<div class="panel-body">
					<form:form method="POST" modelAttribute="pessoa" id="form-editar-pessoa">
						<form:hidden path="idPessoa" />
						<fieldset class="form-group">
							<form:label path="nome">Nome</form:label>
							<form:input path="nome" type="text" class="form-control"
								required="required" />
							<form:errors path="nome" cssClass="text-warning" />
						</fieldset>

						<fieldset class="form-group">
							<form:label path="dataNascimento">Data Nascimento</form:label>
							<form:input path="dataNascimento" type="date" class="form-control"
								required="required" />
							<form:errors path="dataNascimento" cssClass="text-warning" />
						</fieldset>
						
						<fieldset class="form-group">
							<form:label path="cpf">CPF</form:label>
							<form:input path="cpf" maxlength="14" type="text" class="form-control"
								required="required" />
							<form:errors path="cpf" cssClass="text-warning" />
						</fieldset>
						
						<fieldset class="form-group">
							<form:checkbox path="gerente" class="form-control" label="Gerente" />
							<form:errors path="gerente" cssClass="text-warning" />
						</fieldset>

						<button type="submit" class="btn btn-success" id="btn-editar-pessoa">Gravar</button>
					</form:form>
				</div>
			</div>
		</div>
	</div>
</div>
<%@ include file="common/footer.jspf"%>