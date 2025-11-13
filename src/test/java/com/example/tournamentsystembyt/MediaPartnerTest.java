package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.model.MediaPartner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class MediaPartnerTest {

    @Test
    void ctor_rejectsEmptyNameEmailType() {
        assertThrows(InvalidValueException.class, () -> new MediaPartner(" ", "a@b.com", "TV"));
        assertThrows(InvalidValueException.class, () -> new MediaPartner("ESPN", " ", "TV"));
        assertThrows(InvalidValueException.class, () -> new MediaPartner("ESPN", "a@b.com", " "));
    }

    @Test
    void ctor_rejectsInvalidEmail() {
        assertThrows(InvalidValueException.class, () -> new MediaPartner("ESPN", "not-an-email", "TV"));
    }
}