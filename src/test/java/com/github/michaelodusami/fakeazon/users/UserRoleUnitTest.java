package com.github.michaelodusami.fakeazon.users;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import com.github.michaelodusami.fakeazon.modules.user.enums.UserRole;

class UserRoleUnitTest {
    
    @Test
    void fromString_suceeds()
    {
        UserRole role = UserRole.fromString("ADMIN");
        assertEquals(role, UserRole.ROLE_ADMIN);
    }

    @Test
    void fromString_fails()
    {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> UserRole.fromString("ROLE_ADMIN"));
        assertNotNull(exception);
    }
}
