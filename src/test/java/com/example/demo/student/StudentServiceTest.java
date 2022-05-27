package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import com.example.demo.student.exception.StudentNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    private StudentService underTest;
//    private  AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        System.out.println("-----------------------> before each <----------------------------------");
//        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new StudentService(studentRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        System.out.println("-----------------------> after each <----------------------------------");
//            autoCloseable.close();
    }

    @Test
    void getAllStudents() {
        //given

        //when
            underTest.getAllStudents();
        //then
            verify(studentRepository).findAll();//pass
//            verify(studentRepository).deleteAll();//fail
    }

    @Test
    void canAddStudent() {
        //given
        String email = "jemila@gmail.com";
        Student student = new Student(
                "Jamila",
                        email,
                        Gender.FEMALE
        );
        //when
         underTest.addStudent(student);
        //then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository)
                .save(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(student);
    }


    @Test
    void willThrowWhenEmailIsTaken() {
        //given

        String email = "jemila@gmail.com";
        Student student = new Student(
                "Jamila",
                email,
                Gender.FEMALE
        );
        given(studentRepository.selectExistsEmail(anyString())) //student.getemail
                .willReturn(true);
        //when
        //then
        assertThatThrownBy(()->underTest.addStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email " + student.getEmail() + " taken");
        verify(studentRepository,never()).save(any());
    }

    @Test
    void deleteStudent() {
        //given
        long studentId = 1L;
        given(studentRepository.existsById(studentId))
                .willReturn(false);
         //when
         //then
        assertThatThrownBy(()->underTest.deleteStudent(studentId))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining( "Student with id " + studentId + " does not exists");
        verify(studentRepository,never()).save(any());
//        underTest.deleteStudent(studentId);
    }
}