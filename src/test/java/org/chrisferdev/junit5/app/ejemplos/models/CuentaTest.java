package org.chrisferdev.junit5.app.ejemplos.models;

import org.chrisferdev.junit5.app.ejemplos.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {
    Cuenta cuenta;

    @BeforeEach
    void initMetodoTest(){
        this.cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
        System.out.println("iniciando el metodo.");
    }

    @AfterEach
    void tearDown() {
        System.out.println("finalizando el método de prueba.");
    }

    @Test
    @DisplayName("probando nombre de la cuenta corriente!")
    void testNombreCuenta() {
        //cuenta.setPersona("Andres");
        String esperado = "Andres";
        String real = cuenta.getPersona();
        assertNotNull(real, () -> "La cuenta no puede ser nula");
        assertEquals(esperado, real, "El nombre de la cuenta no es el que se esperaba: se esperaba " + esperado
                + " sin embargo fue " + real);
        assertTrue(real.equals("Andres"), "nombre cuenta esperada debe ser igual a la real");
    }

    @Test
    @DisplayName("probando el saldo de la cuenta corriente, que no sea null, mayor que cero, valor esperado.")
    void testSaldoCuenta() {
        cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("testeando referencias que sean iguales con el método equals.")
    void testReferenciaCuenta() {
        cuenta = new Cuenta("John Doe", new BigDecimal("8900.9997"));
        Cuenta cuenta2 = new Cuenta("John Doe", new BigDecimal("8900.9997"));

        //assertNotEquals(cuenta2, cuenta);
        assertEquals(cuenta2, cuenta);
    }

    @Test
    void testDebitoCuenta() {
        cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testCreditoCuenta() {
        cuenta.credito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue());
        assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testDineroInsuficienteExceptionCuenta() {
        Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
            cuenta.debito(new BigDecimal(1500));
        });
        String actual = exception.getMessage();
        String esperado = "Dinero Insuficiente";
        assertEquals(esperado, actual);
    }

    @Test
    void testTransferirDineroCuentas() {
        Cuenta cuenta1 = new Cuenta("John Doe", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("1500.8989"));

        Banco banco = new Banco();
        banco.setNombre("Banco del Estado");
        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));
        assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
        assertEquals("3000", cuenta1.getSaldo().toPlainString());
    }

    @Test
    //@Disabled
    @DisplayName("probando relaciones entre las cuentas y el banco con assertAll.")
    void testRelacionBancosCuentas() {
        //fail();
        Cuenta cuenta1 = new Cuenta("John Doe", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("1500.8989"));

        Banco banco = new Banco();
        banco.addCuentas(cuenta1);
        banco.addCuentas(cuenta2);

        banco.setNombre("Banco del Estado");
        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));

        assertAll(() -> assertEquals("1000.8989", cuenta2.getSaldo().toPlainString(),
                                    () -> "el valor del saldo de la cuenta2 no es el esperado."),
                () -> assertEquals("3000", cuenta1.getSaldo().toPlainString(),
                                    () -> "el valor del saldo de la cuenta1 no es el esperado."),
                () -> assertEquals(2, banco.getCuentas().size(),
                                    () -> "el banco no tiene las cuentas esperadas."),
                () -> assertEquals("Banco del Estado", cuenta1.getBanco().getNombre()),
                () -> assertEquals("Andres", banco.getCuentas().stream()
                        .filter(c -> c.getPersona().equals("Andres"))
                        .findFirst()
                        .get().getPersona()),
                () -> assertTrue(banco.getCuentas().stream()
                        .anyMatch(c -> c.getPersona().equals("John Doe"))));
    }
}