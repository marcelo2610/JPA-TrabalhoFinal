Trabalho Final JPA - PÓS-GRADUAÇÃO EM DESENVOLVIMENTO FULL STACK 2017 - FACULDADE DELTA

Qual a responsabilidade/objeto das anotações:

@MappedSuperclass: 
@Version: Funciona como um controle de versão da entidade, ao tentar ser persistida, é verificada a versão atual com a versão anterior da
@Entity: define que uma classe Java será uma entidade que pode ser persistida

@Table: Define o nome de tabela que uma entidade Java terá, se não for utilizada, o nome da tabela será igual ao nome da classe.

@Id: define qual coluna será a chave primária de uma entidade Java.
<<<<<<< HEAD

@GeneratedValue: especifica um  valor que será gerado automaticamente para a coluna de uma entidade, geralmente usado para definir valores de colunas que são chaves primárias.

@Column: define algumas características de uma coluna de uma entidade Java, como nome, se pode ser nula ou não, tamanho e se é de valor único (unique).

@Basic: pode ser utilizado para definir se uma coluna da entidade pode ser nula ou não e também se o carregamento de seu conteúdo em uma consulta será LAZY ou EAGER.

=======
@GeneratedValue: especifica um  valor que será gerado automaticamente para a coluna de uma entidade, geralmente usado para definir valores de colunas que são chaves primárias.
@Column: define algumas características de uma coluna de uma entidade Java, como nome, se pode ser nula ou não, tamanho e se é de valor único (unique).
@Basic: pode ser utilizado para definir se uma coluna da entidade pode ser nula ou não e também se o carregamento de seu conteúdo em uma consulta será LAZY ou EAGER.
>>>>>>> f9aa4059cf056b88861bb6bee2be3fab4a345773
@Temporal: define qual o formato de data que um atributo do tipo Date de uma classe Java que é uma entidade terá, 
           podendo ser DATE (somente data, no formada DD/MM/AAAA), TIME (somente a hora no formado HH:MM:SS) ou TIMESTAMP (data e hora no formato DD/MM/AAAA HH:MM:SS).
		   
Qual a responsabilidade/objeto das anotações:

@ManyToOne: mapear o relacionamento entre entidades de muitos-para-um.
@ManyToMany: mapear o relacionamento entre entidades de muitos para muitos.
@OneToOne: mapear bi-direcionalmente o relacionamento entre entidades de um para um.
@JoinColumn: 
@JoinTable: cria uma tabela de associação que faz o relacionamento entre outras duas tabelas no JPA.

Qual a responsabilidade/objeto dos métodos do EntityManager:
isOpen: verifica se uma entidade está aberta.
close: fecha uma entidade que está aberta.
createQuery: cria uma consulta JPQL a ser executada pelo JPA. OBS: muito parecida com a linguagem SQL.
find: executa a busca (consulta) à um objeto ou uma coleção de objetos de uma entidade.
merge: executa a alteração de dados de um objeto de uma entidade.
persist
remove
	   
Como instânciar Criteria do Hibernate através do EntityManager?
Dê exemplo do código
Como abrir uma transação?
	em.getTransaction().begin();
	
Como fechar uma transação?
	public void fecharEntityManager() {
		if (em.isOpen()) {
			em.close();
		}
	}
Como criar e executar uma query com JPQL?
		StringBuilder jpql = new StringBuilder();
		jpql.append(" SELECT COUNT(p.id) ");
		jpql.append("   FROM Venda v ");
		jpql.append("  INNER JOIN v.produtos p ");
		jpql.append("  INNER JOIN v.cliente c ");
		jpql.append("  WHERE c.cpf = :cpf ");
		
		Query query = em.createQuery(jpql.toString());
		query.setParameter("cpf", "001.001.001-01");

		Long qtdProdutosDaVenda = (Long) query.getSingleResult();
		
Qual a responsabilidade dos valores FetchType.LAZY e FetchType.EAGER?
Qual a responsabilidade dos valores CascadeType.PERSIST e CascadeType.REMOVE?
Como fazer uma operação BATCH (DELETE ou UPDATE) através do EntityManager?
Qual a explicação para a exception LazyInitializationException?
