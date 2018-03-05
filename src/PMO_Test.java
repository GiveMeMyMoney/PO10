import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.naming.OperationNotSupportedException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PMO_Test {

	//////////////////////////////////////////////////////////////////////////
	private static final Map<String, Double> tariff = new HashMap<>();

	static {
		tariff.put("linkTest", 2.0);
		tariff.put("linkChangeTest", 2.0);
		tariff.put("linkNullTest", 2.0);
		tariff.put("lockTest", 2.0);
		tariff.put("lockTestO", 2.0);
		tariff.put("linkTestO", 2.0);
		tariff.put("linkChangeTestO", 2.0);
		tariff.put("linkChangeTestOBis", 2.0);
	}

	public static double getTariff(String testName) {
		return tariff.get(testName);
	}
	//////////////////////////////////////////////////////////////////////////

	private VariablesDeclarationsInterface vd;
	private Observer observer;
	private static final VariablesDeclarationsInterface.DataType INT_T = VariablesDeclarationsInterface.DataType.INT;
	private static final VariablesDeclarationsInterface.DataType LONG_T = VariablesDeclarationsInterface.DataType.LONG;
	private static final VariablesDeclarationsInterface.DataType DOUBLE_T = VariablesDeclarationsInterface.DataType.DOUBLE;
	private static final VariablesDeclarationsInterface.DataType BOOLEAN_T = VariablesDeclarationsInterface.DataType.BOOLEAN;

	@Before
	public void prepare() {
		vd = (VariablesDeclarationsInterface) PMO_GeneralPurposeFabric.fabric("VariablesDeclarations",
				"VariablesDeclarationsInterface");
		observer = new Observer();
	}

	private void datatypeTest(String name, VariablesDeclarationsInterface.DataType expected,
			Optional<VariablesDeclarationsInterface.DataType> received) {
		assertTrue("Obiekt Optional powinien zawierac typ zmiennej o nazwie " + name, received.isPresent());

		assertEquals("Oczekiwany typ danej jest bledny", expected, received.get());

		Map<String, VariablesDeclarationsInterface.DataType> variables = PMO_TestHelper
				.tryExecute(() -> vd.getDeclaredVariables(), "getDeclaredVariables");

		assertThat("Mapa zadeklarowanych zmiennych nie zawiera wpisu dla " + name, variables, hasEntry(name, expected));
	}

	private void link(String variableName, VariablesDeclarationsInterface.DataType type,
			VariablesDeclarationsInterface.DataType expectedType, boolean force, boolean operationNotSupportedException,
			boolean variableAlreadyExistsException, boolean invalidCastException) {
		PMO_TestHelper.tryExecute(() -> {

			try {
				vd.link(variableName, type, force);
			} catch (OperationNotSupportedException e) {
				if (!operationNotSupportedException) {
					fail("Blednie wygenerowano wyjatek OperationNotSupportedException");
				}
			} catch (VariablesDeclarationsInterface.VariableAlreadyExistsException e) {
				if (!variableAlreadyExistsException) {
					fail("Blednie wygenerowano wyjatek VariableAlreadyExistsException");
				}
			} catch (VariablesDeclarationsInterface.InvalidCastException e) {
				if (!invalidCastException) {
					fail("Blednie wygenerowano wyjatek InvalidCastException");
				}
			}
		}, "link");

		Optional<VariablesDeclarationsInterface.DataType> getType;
		getType = PMO_TestHelper.tryExecute(() -> vd.getType(variableName), "getType");
		if (operationNotSupportedException || variableAlreadyExistsException || invalidCastException) {
			// wystapil wyjatek

			if (variableAlreadyExistsException) {
				datatypeTest(variableName, expectedType, getType);
			} else {
				// nie powinno byc wyniku
				assertFalse("getType nie powinien zwrocic wyniku", getType.isPresent());
			}

		} else {
			// nie bylo wyjatku, wiec ten typ powinien byc znany
			datatypeTest(variableName, expectedType, getType);
		}

	}

	private void link(String variableName, VariablesDeclarationsInterface.DataType type,
			VariablesDeclarationsInterface.DataType expectedType, boolean force) {
		link(variableName, type, expectedType, force, false, false, false);
	}

	@Test
	public void linkTest() {
		link("integer1", INT_T, INT_T, false);
		link("long1", LONG_T, LONG_T, false);
		link("double1", DOUBLE_T, DOUBLE_T, false);
		link("boolean1", BOOLEAN_T, BOOLEAN_T, false);
	}

	@Test
	public void linkChangeTest() {
		link("integer1", INT_T, INT_T, false);
		link("integer1", LONG_T, INT_T, false, false, true, false);
		link("integer1", LONG_T, LONG_T, true);
	}

	@Test
	public void linkNullTest() {
		link("integer1", null, null, false, true, false, false);
	}

	@Test
	public void lockTest() {
		link("integer1", INT_T, INT_T, false);
		assertFalse("System nie zostal zablokowany, a twierdzi, ze lock zostal zalozony",
				PMO_TestHelper.tryExecute(() -> vd.isLocked(), "isLocked"));
		PMO_TestHelper.tryExecute(() -> vd.lockTypes(), "lockTypes");
		assertTrue("System zostal zablokowany, a twierdzi, ze lock nie zostal zalozony",
				PMO_TestHelper.tryExecute(() -> vd.isLocked(), "isLocked"));
		link("integer2", INT_T, INT_T, false, true, false, false);
	}

	private class Observer implements VariablesDeclarationsInterface.DeclarationObserverInterface {

		private boolean locked;
		private boolean newVariable;
		private boolean removeVariable;
		private boolean typeChange;
		private String variableName;
		private VariablesDeclarationsInterface.DataType type;
		private VariablesDeclarationsInterface.DataType oldType;

		@Override
		public void declarationsLocked() {
			locked = true;
		}

		@Override
		public void newVariable(String variableName, VariablesDeclarationsInterface.DataType type) {
			this.variableName = variableName;
			this.type = type;
			newVariable = true;
		}

		@Override
		public void removeVariable(String variableName, VariablesDeclarationsInterface.DataType type) {
			this.variableName = variableName;
			this.type = type;
			removeVariable = true;
		}

		@Override
		public void typeChange(String variableName, VariablesDeclarationsInterface.DataType oldType,
				VariablesDeclarationsInterface.DataType newType) {
			this.variableName = variableName;
			this.type = newType;
			this.oldType = oldType;
			typeChange = true;
		}

	}

	private void addObserver() {
		PMO_TestHelper.tryExecute(() -> vd.addObserver(observer), "addObserver");
	}

	@Test
	public void linkTestO() {
		addObserver();
		link("integer1", INT_T, INT_T, false);
		assertTrue("Obserwator nie zostal powiadomiony", observer.newVariable);
		assertEquals("Obserator otrzymal bledna nazwe zmiennej", observer.variableName, "integer1");
		assertEquals("Obserator otrzymal bledny type zmiennej", observer.type, INT_T);
	}

	@Test
	public void linkChangeTestO() {
		addObserver();
		link("integer1", INT_T, INT_T, false);
		link("integer1", LONG_T, LONG_T, true);
		assertTrue("Obserwator nie zostal powiadomiony o zmianie typu", observer.typeChange);
		assertEquals("Obserator otrzymal bledna nazwe modyfikowanej zmiennej", observer.variableName, "integer1");
		assertEquals("Obserator otrzymal bledny stary typ zmiennej", observer.oldType, INT_T);
		assertEquals("Obserator otrzymal bledny nowy typ zmiennej", observer.type, LONG_T);
	}

	@Test
	public void linkChangeTestOBis() {
		addObserver();
		link("integer1", LONG_T, LONG_T, false);
		link("integer1", LONG_T, LONG_T, true);
		assertFalse("Obserwator nie powinien zostac powiadomiony o zmianie typu", observer.typeChange);
		assertTrue("Obserwator powinien zostac powiadomiony za pomoca pary metod", observer.removeVariable);
		assertTrue("Obserwator powinien zostac powiadomiony za pomoca pary metod", observer.newVariable);
	}

	@Test
	public void lockTestO() {
		addObserver();
		PMO_TestHelper.tryExecute(() -> vd.lockTypes(), "lockTypes");
		assertTrue("Obserwator powinien zostac powiadomiony o blokadzie", observer.locked);
	}

}