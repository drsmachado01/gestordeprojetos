<%@ include file="common/header.jspf"%>
<%@ include file="common/navigation.jspf"%>
<div class="container">
	<div class="row">
		<div class="col-md-6 col-md-offset-3 ">
			<div class="panel panel-primary">
				<div class="panel-heading">Adicionar Projeto</div>
				<div class="panel-body">
					<form:form method="post" modelAttribute="projeto" id="form-adicionar-projeto">
						<form:hidden path="idProjeto" />
						<fieldset class="form-group">
							<form:label path="nome">Nome</form:label>
							<form:input path="nome" type="text" class="form-control"
								required="required" />
							<form:errors path="nome" cssClass="text-warning" />
						</fieldset>
						<fieldset class="form-group">
							<form:label path="descricao">Descrição</form:label>
							<form:input path="descricao" type="text" class="form-control"
								required="required" />
							<form:errors path="descricao" cssClass="text-warning" />
						</fieldset>

						<fieldset class="form-group">
							<form:label path="dataInicio">Data Início</form:label>
							<form:input path="dataInicio" type="date" class="form-control"
								required="required" />
							<form:errors path="dataInicio" cssClass="text-warning" />
						</fieldset>

						<fieldset class="form-group">
							<form:label path="dataPrevisaoFim">Data Previsão Fim</form:label>
							<form:input path="dataPrevisaoFim" type="date" class="form-control"
								required="required" />
							<form:errors path="dataPrevisaoFim" cssClass="text-warning" />
						</fieldset>

						<fieldset class="form-group">
							<form:label path="dataFim">Data Fim</form:label>
							<form:input path="dataFim" type="date" class="form-control"
								required="required" />
							<form:errors path="dataFim" cssClass="text-warning" />
						</fieldset>
						
						<fieldset class="form-group">
							<form:label path="orcamento">Orçamento</form:label>
							<form:input path="orcamento" maxlength="14" type="text" class="form-control"
								required="required" />
							<form:errors path="orcamento" cssClass="text-warning" />
						</fieldset>
						
						<fieldset class="form-group">
							<form:label path="risco">Risco</form:label>
							<form:input path="risco" maxlength="14" type="text" class="form-control"
								required="required" />
							<form:errors path="risco" cssClass="text-warning" />
						</fieldset>
						
						<fieldset class="form-group">
							<form:label path="gerente">Gerente</form:label>
							<form:select path="gerente" class="form-control" id="select-gerente">
								<form:option value="">Selecione o gerente</form:option>
							</form:select>
							<form:errors path="gerente" cssClass="text-warning" />
						</fieldset>

						<button type="submit" class="btn btn-success" id="btn-adicionar-projeto">Gravar</button>
					</form:form>
				</div>
			</div>
		</div>
	</div>
</div>
<%@ include file="common/footer.jspf"%>
<script>	
	jQuery(document).ready(function($){
		$.ajax({
			url: 'http://localhost:8080/listar-gerentes',
			type: 'GET',
			contentType : "application/json",
			dataType : 'json',
			success: function(response) {
				let gerentes = response;
				for(i in gerentes) {
					let g = gerentes[i];
					$('#select-gerente').append(
						$('<option>', {
							value: g.idPessoa,
							text: g.nome
						})
					);
				}
			}, error: function(e) {
				console.log('Erro ao buscar gerentes')
				console.log(e)
			}
		});
	});
</script>