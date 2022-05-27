package com.example.demo.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class StudentRepositoryTest {
//testing student repository
    @Autowired
    private StudentRepository underTest;

    @BeforeEach
    void setUp() {
        System.out.println("-----------------------test started----------------------------------");
        System.out.println("-----------------------test started----------------------------------");
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldCheckIfStudentExistsEmail() {
        //given
        String email = "jemila@gmail.com";
            Student student = new Student(
                    "Jamila",
                    email,
                    Gender.FEMALE
            );
            underTest.save(student);
        //when
        Boolean exists = underTest.selectExistsEmail(email);
        //then
        assertThat(exists).isTrue();
    }

    @Test
    void itShouldCheckIfStudentEmailDoesNotExist() {
        //given
        String email = "jemila@gmail.com";
        //when
        Boolean exists = underTest.selectExistsEmail(email);
        //then
        assertThat(exists).isFalse();
    }
}