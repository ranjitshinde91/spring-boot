package com.ranjit.shinde.springbootdemo.controller;


import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.RestPactRunner;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactUrl;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.target.MockMvcTarget;
import com.ranjit.shinde.springbootdemo.advice.RestResponseExceptionEntityHandler;
import com.ranjit.shinde.springbootdemo.exception.MovieNotFoundException;
import com.ranjit.shinde.springbootdemo.model.Movie;
import com.ranjit.shinde.springbootdemo.service.MovieService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RestPactRunner.class)
@Provider("movie-service")
@PactUrl(urls = {"https://s3.ap-south-1.amazonaws.com/pan-service-pact-contracts/movie-consumer-movie-service.json"})
public class MovieControllerProviderTest {

    @Mock
    private MovieService movieService;

    private MovieController movieController;

    @TestTarget
    public final MockMvcTarget target = new MockMvcTarget();


    @Before
    public void before() {
        initMocks(this);
        movieController = new MovieController(movieService);
        target.setControllers(movieController);
        target.setControllerAdvice(new RestResponseExceptionEntityHandler());
    }

    @State("getMovie")
    public void movieData() {
        Movie movie = new Movie("LOTR",
                LocalDate.of(2006, 05, 26), 123);
        when(movieService.get(anyString())).thenReturn(movie);
    }

    @State("getMissingMovie")
    public void movieDataNotFound(){
        when(movieService.get(anyString())).thenThrow(new MovieNotFoundException("DDLJ2"));
    }

}
