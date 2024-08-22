package com.example.rest_with_spring_boot.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.rest_with_spring_boot.aux_math.Converters;
import com.example.rest_with_spring_boot.aux_math.SimpleMath;
import com.example.rest_with_spring_boot.exceptions.UnsupportedMathOperationException;

@RestController
public class MathController{
    private final SimpleMath math = new SimpleMath();

    @RequestMapping(value="/sum/{numberOne}/{numberTwo}", method=RequestMethod.GET)
    public Double sum(
        @PathVariable(value="numberOne") String numberOne,
        @PathVariable(value="numberTwo") String numberTwo
        ) throws Exception {
        if (!Converters.isNumeric(numberOne) || !Converters.isNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException("Please set a numeric value!");
        }
        return math.sum(Converters.convertToDouble(numberOne), Converters.convertToDouble(numberTwo));
        }

    @RequestMapping(value="/sub/{numberOne}/{numberTwo}", method=RequestMethod.GET)
    public Double sub(
        @PathVariable(value="numberOne") String numberOne,
        @PathVariable(value="numberTwo") String numberTwo
        ) throws Exception {
        if (!Converters.isNumeric(numberOne) || !Converters.isNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException("Please set a numeric value!");
        }
        return math.sub(Converters.convertToDouble(numberOne), Converters.convertToDouble(numberTwo));
        }

    @RequestMapping(value="/mult/{numberOne}/{numberTwo}", method=RequestMethod.GET)
    public Double mult(
        @PathVariable(value="numberOne") String numberOne,
        @PathVariable(value="numberTwo") String numberTwo
        ) throws Exception {
        if (!Converters.isNumeric(numberOne) || !Converters.isNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException("Please set a numeric value!");
        }
        return math.mult(Converters.convertToDouble(numberOne), Converters.convertToDouble(numberTwo));
        }

    @RequestMapping(value="/div/{numberOne}/{numberTwo}", method=RequestMethod.GET)
    public Double div(
        @PathVariable(value="numberOne") String numberOne,
        @PathVariable(value="numberTwo") String numberTwo
        ) throws Exception {
        if (!Converters.isNumeric(numberOne) || !Converters.isNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException("Please set a numeric value!");
        }
        return math.div(Converters.convertToDouble(numberOne), Converters.convertToDouble(numberTwo));
        }

    @RequestMapping(value="/med/{numberOne}/{numberTwo}", method=RequestMethod.GET)
    public Double med(
        @PathVariable(value="numberOne") String numberOne,
        @PathVariable(value="numberTwo") String numberTwo
        ) throws Exception {
        if (!Converters.isNumeric(numberOne) || !Converters.isNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException("Please set a numeric value!");
        }
        return math.med(Converters.convertToDouble(numberOne), Converters.convertToDouble(numberTwo));
        }

    @RequestMapping(value="/raiz/{numberOne}", method=RequestMethod.GET)
    public Double raiz(@PathVariable(value="numberOne") String numberOne)
        throws Exception {
        if (!Converters.isNumeric(numberOne)) {
            throw new UnsupportedMathOperationException("Please set a numeric value!");
        }
        return math.raiz(Converters.convertToDouble(numberOne));
        }
}
