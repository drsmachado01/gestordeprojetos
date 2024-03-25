package br.com.darlan.gestordeprojetos.util;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.darlan.gestordeprojetos.dto.PessoaDTO;
import br.com.darlan.gestordeprojetos.dto.ProjetoDTO;
import br.com.darlan.gestordeprojetos.model.Pessoa;
import br.com.darlan.gestordeprojetos.model.Projeto;
import br.com.darlan.gestordeprojetos.model.StatusProjeto;

public class TesteUtil {

    public static PessoaDTO buildFuncionario() {
        return PessoaDTO.builder()
                .nome("Joaquim da Silva")
                .cpf("12312312312")
                .dataNascimento(ConverterUtil.toString(LocalDate.of(1980, 10,20), ConverterUtil.DATE_PATTERN))
                .funcionario(true)
                .gerente(false)
                .build();
    }

    public static PessoaDTO buildGerente() {
        return PessoaDTO.builder()
                .nome("Carlos da Silva")
                .cpf("32132132132")
                .dataNascimento(ConverterUtil.toString(LocalDate.of(1980, 10,20), ConverterUtil.DATE_PATTERN))
                .funcionario(false)
                .gerente(true)
                .build();
    }

    public static Pessoa buildPessoaFuncionario() {
        return Pessoa.builder()
                .nome("Joaquim da Silva")
                .cpf("12312312312")
                .dataNascimento(LocalDate.of(1980, 10,20))
                .funcionario(true)
                .gerente(false)
                .build();
    }

    public static Pessoa buildPessoaGerente() {
        return Pessoa.builder()
                .nome("Carlos da Silva")
                .cpf("32132132132")
                .dataNascimento(LocalDate.of(1980, 10,20))
                .funcionario(false)
                .gerente(true)
                .build();
    }

    public static Projeto buildProjeto(String statusProjeto, String risco) {
        return Projeto.builder()
                .idProjeto(1L)
                .nome("Projeto 1")
                .descricao("Projeto de teste")
                .gerente(buildPessoaGerente())
                .dataInicio(LocalDate.of(2022, 10, 10))
                .dataPrevisaoFim(LocalDate.of(2023, 10, 10))
                .dataFim(LocalDate.of(2023, 5, 10))
                .status(StatusProjeto.fromString(statusProjeto))
                .orcamento(BigDecimal.valueOf(1000.0))
                .risco(risco)
                .build();
    }

    public static ProjetoDTO buildProjetoDTO(String statusProjeto, String risco) {
        return ProjetoDTO.builder()
                .idProjeto(1L)
                .nome("Projeto 1")
                .descricao("Projeto de teste")
                .gerente(buildGerente().getIdPessoa())
                .dataInicio(ConverterUtil.toDate(LocalDate.of(2022, 10, 10)))
                .dataPrevisaoFim(ConverterUtil.toDate(LocalDate.of(2023, 10, 10)))
                .dataFim(ConverterUtil.toDate(LocalDate.of(2023, 5, 10)))
                .status(statusProjeto)
                .orcamento(BigDecimal.valueOf(1000.0))
                .risco(risco)
                .build();
    }
}
