package integracao.rest.contatos;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@DataJpaTest
@RunWith(SpringRunner.class)
public class ContatoRepositoryIntegrationTest {

	@Autowired
	private ContatoRepository repository;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	private Contato contato;

	@Before
	public void start() {
		this.contato = new Contato("Bruno", "11", "11955551111");
	}

	@Test
	public void saveWithNomeNullShouldThrowException() {
		expectedException.expect(ConstraintViolationException.class);
		expectedException.expectMessage("O Nome deve ser preenchido");

		contato.setNome(null);
		repository.save(contato);
	}

	@Test
	public void saveWithDddNullShouldThrowException() {
		expectedException.expect(ConstraintViolationException.class);
		expectedException.expectMessage("O DDD deve ser preenchido");

		contato.setDdd(null);
		repository.save(contato);
	}

	@Test
	public void saveWithTelefoneNullShouldThrowException() {
		expectedException.expect(ConstraintViolationException.class);
		expectedException.expectMessage("O Telefone deve ser preenchido");

		contato.setTelefone(null);
		repository.save(contato);
	}

	@Test
	public void saveShouldPersistContato() {
		repository.save(contato);
		List<Contato> result = repository.findAll();

		assertEquals(1, result.size());
		repository.deleteAll();
	}

	@Test
	public void deleteByIdShouldRemoveContato() {
		repository.save(contato);
		List<Contato> contatos = repository.findAll();
		assertEquals(1, contatos.size());

		repository.deleteById(contato.getId());
		List<Contato> result = repository.findAll();
		assertEquals(0, result.size());
	}

	@Test
	public void mustUpdateEntitySuccessfully() {
		repository.save(contato);
		Optional<Contato> result = repository.findById(contato.getId());
		assertEquals("Bruno", result.get().getNome());

		contato.setNome("Bruno Teste 2");
		repository.save(contato);
		Optional<Contato> resultWithNameChanged = repository.findById(contato.getId());
		assertEquals("Bruno Teste 2", resultWithNameChanged.get().getNome());
		repository.deleteAll();
	}
}
