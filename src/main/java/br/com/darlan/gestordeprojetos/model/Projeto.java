package br.com.darlan.gestordeprojetos.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "projeto")
@Data
public class Projeto {
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJETO_ID_SEQ")
	@SequenceGenerator(name = "PROJETO_ID_SEQ", sequenceName = "PROJETO_ID_SEQ", allocationSize = 1)
	private Long idProjeto;
	@Column(nullable = false, length = 200)
	private String nome;
	@Column(name = "data_inicio")
	private LocalDate dataInicio;
	@Column(name = "data_previsao_fim")
	private LocalDate dataPrevisaoFim;
	@Column(name = "data_fim")
	private LocalDate dataFim;
	@Column(length = 5000)
	private String descricao;
	@Enumerated(EnumType.STRING)
	@Column(length = 45)
	private StatusProjeto status;
	private BigDecimal orcamento;
	@Column(length = 45)
	private String risco;
	@ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
	@JoinColumn(name = "idgerente")
	private Pessoa gerente;

	@ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
	@JoinTable(name = "membros", joinColumns = @JoinColumn(name = "idprojeto", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "idpessoa", referencedColumnName = "id"))
	private List<Pessoa> membros = new ArrayList<>();

	public void adicionarMembros(Pessoa membro) {
		if(null == this.membros) {
			this.membros = new ArrayList<>();
		}
		this.membros.add(membro);
	}
}
