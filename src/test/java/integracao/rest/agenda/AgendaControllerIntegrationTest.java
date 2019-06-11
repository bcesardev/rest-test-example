package integracao.rest.agenda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import integracao.rest.contatos.Contato;
import integracao.rest.contatos.ContatoRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AgendaControllerIntegrationTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private ContatoRepository contatoRepository;

	private Contato contato;

	private final String nome = "Bruno";

	private final String ddd = "11";

	private final String telefone = "11955554444";

	@Before
	public void start() {
		contato = new Contato(nome, ddd, telefone);
		contatoRepository.save(contato);
	}

	@After
	public void end() {
		contatoRepository.deleteAll();
	}

	@Test
	public void deveMostrarTodosContatos() {
		ResponseEntity<String> resposta = testRestTemplate.exchange("/agenda/", HttpMethod.GET, null, String.class);
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}

	@Test
	public void deveMostrarTodosContatosUsandoString() {
		ResponseEntity<String> resposta = testRestTemplate.exchange("/agenda/", HttpMethod.GET, null, String.class);

		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertTrue(resposta.getHeaders().getContentType()
				.equals(MediaType.parseMediaType("application/json;charset=UTF-8")));

		String result = "[{\"id\":" + contato.getId() + ",\"ddd\":\"11\","
				+ "\"telefone\":\"11955554444\",\"nome\":\"Bruno\"}]";
		assertEquals(result, resposta.getBody());
	}

	@Test
	public void deveMostrarTodosContatosUsandoList() {
		ParameterizedTypeReference<List<Contato>> tipoRetorno = new ParameterizedTypeReference<List<Contato>>() {
		};

		ResponseEntity<List<Contato>> resposta = testRestTemplate.exchange("/agenda/", HttpMethod.GET, null,
				tipoRetorno);

		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertTrue(resposta.getHeaders().getContentType()
				.equals(MediaType.parseMediaType("application/json;charset=UTF-8")));
		assertEquals(1, resposta.getBody().size());
		assertEquals(contato, resposta.getBody().get(0));
	}

	@Test
	public void deveMostrarUmContato() {
		ResponseEntity<Contato> resposta = testRestTemplate.exchange("/agenda/contato/{id}", HttpMethod.GET, null,
				Contato.class, contato.getId());

		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertTrue(resposta.getHeaders().getContentType()
				.equals(MediaType.parseMediaType("application/json;charset=UTF-8")));
		assertEquals(contato, resposta.getBody());
	}

	@Test
	public void buscaUmContatoDeveRetornarNaoEncontrado() {
		ResponseEntity<Contato> resposta = testRestTemplate.exchange("/agenda/contato/{id}", HttpMethod.GET, null,
				Contato.class, 100);

		assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
		assertNull(resposta.getBody());
	}

	@Test
	public void deveMostrarUmContatoComGetForEntity() {
		ResponseEntity<Contato> resposta = testRestTemplate.getForEntity("/agenda/contato/{id}", Contato.class,
				contato.getId());

		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertTrue(resposta.getHeaders().getContentType()
				.equals(MediaType.parseMediaType("application/json;charset=UTF-8")));
		assertEquals(contato, resposta.getBody());
	}

	@Test
	public void deveMostrarUmContatoComGetForObjetc() {
		Contato resposta = testRestTemplate.getForObject("/agenda/contato/{id}", Contato.class, contato.getId());
		assertEquals(contato, resposta);
	}

	@Test
	public void buscaUmContatoDeveRetornarNaoEncontradoComGetForEntity() {
		ResponseEntity<Contato> resposta = testRestTemplate.getForEntity("/agenda/contato/{id}", Contato.class, 100);
		assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
		assertNull(resposta.getBody());
	}

	@Test
	public void buscaUmContatoDeveRetornarNaoEncontradoGetForObjetc() {
		Contato resposta = testRestTemplate.getForObject("/agenda/contato/{id}", Contato.class, 100);
		assertNull(resposta);
	}

	@Test
	public void salvarContatoDeveRetornarMensagemDeErro() {
		Contato contato = new Contato(nome, null, null);
		HttpEntity<Contato> httpEntity = new HttpEntity<>(contato);

		ResponseEntity<List<String>> resposta = testRestTemplate.exchange("/agenda/inserir", HttpMethod.POST,
				httpEntity, new ParameterizedTypeReference<List<String>>() {
				});

		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
		assertTrue(resposta.getBody().contains("O DDD deve ser preenchido"));
		assertTrue(resposta.getBody().contains("O Telefone deve ser preenchido"));
	}

	@Test
	public void inserirDeveSalvarContato() {
		Contato contato = new Contato(nome, ddd, telefone);
		HttpEntity<Contato> httpEntity = new HttpEntity<>(contato);

		ResponseEntity<Contato> resposta = testRestTemplate.exchange("/agenda/inserir", HttpMethod.POST, httpEntity,
				Contato.class);

		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());

		Contato resultado = resposta.getBody();

		assertNotNull(resultado.getId());
		assertEquals(contato.getNome(), resultado.getNome());
		assertEquals(contato.getDdd(), resultado.getDdd());
		assertEquals(contato.getTelefone(), resultado.getTelefone());
	}

	@Test
	public void inserirDeveSalvarContatoComPostForEntity() {
		Contato contato = new Contato(nome, ddd, telefone);
		HttpEntity<Contato> httpEntity = new HttpEntity<>(contato);

		ResponseEntity<Contato> resposta = testRestTemplate.postForEntity("/agenda/inserir", httpEntity, Contato.class);

		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());

		Contato resultado = resposta.getBody();

		assertNotNull(resultado.getId());
		assertEquals(contato.getNome(), resultado.getNome());
		assertEquals(contato.getDdd(), resultado.getDdd());
		assertEquals(contato.getTelefone(), resultado.getTelefone());
	}

	@Test
	public void inserirDeveSalvarContatoComPostForObject() {
		Contato contato = new Contato(nome, ddd, telefone);
		HttpEntity<Contato> httpEntity = new HttpEntity<>(contato);

		Contato resposta = testRestTemplate.postForObject("/agenda/inserir", httpEntity, Contato.class);

		assertNotNull(resposta.getId());
		assertEquals(contato.getNome(), resposta.getNome());
		assertEquals(contato.getDdd(), resposta.getDdd());
		assertEquals(contato.getTelefone(), resposta.getTelefone());
	}

	@Test
	public void alterarDeveRetornarMensagemDeErro() {
		contato.setDdd(null);
		contato.setTelefone(null);
		HttpEntity<Contato> httpEntity = new HttpEntity<Contato>(contato);
		ResponseEntity<List<String>> resposta = testRestTemplate.exchange("/agenda/alterar/{id}", HttpMethod.PUT,
				httpEntity, new ParameterizedTypeReference<List<String>>() {
				}, contato.getId());

		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
		assertTrue(resposta.getBody().contains("O DDD deve ser preenchido"));
		assertTrue(resposta.getBody().contains("O Telefone deve ser preenchido"));
	}

	@Test
	public void alterarDeveAlterarContato() {
		contato.setNome("Bruno 2");
		HttpEntity<Contato> httpEntity = new HttpEntity<Contato>(contato);
		ResponseEntity<Contato> resposta = testRestTemplate.exchange("/agenda/alterar/{id}", HttpMethod.PUT, httpEntity,
				Contato.class, contato.getId());

		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());

		Contato resultado = resposta.getBody();
		assertNotNull(resultado.getId());
		assertEquals(contato.getDdd(), resultado.getDdd());
		assertEquals(contato.getTelefone(), resultado.getTelefone());
		assertEquals("Bruno 2", resultado.getNome());
	}

	@Test
	public void alterarDeveAlterarContatoComPut() {
		contato.setNome("Bruno 3");
		testRestTemplate.put("/agenda/alterar/{id}", contato, contato.getId());

		Contato resultado = contatoRepository.findById(contato.getId()).get();
		assertEquals(ddd, resultado.getDdd());
		assertEquals(telefone, resultado.getTelefone());
		assertEquals("Bruno 3", resultado.getNome());
	}

	@Test
	public void removerDeveExcluirContato() {
		ResponseEntity<Contato> resposta = testRestTemplate.exchange("/agenda/remover/{id}", HttpMethod.DELETE, null,
				Contato.class, contato.getId());

		assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());
		assertNull(resposta.getBody());
	}

	@Test
	public void removerDeveExcluirContatoComDelete() {
		testRestTemplate.delete("/agenda/remover/" + contato.getId());

		Optional<Contato> resultado = contatoRepository.findById(contato.getId());
		assertEquals(Optional.empty(), resultado);
	}
}
