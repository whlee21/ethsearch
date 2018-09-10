package io.blocko.ethsearch.cucumber.stepdefs;

import io.blocko.ethsearch.EthsearchApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = EthsearchApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
