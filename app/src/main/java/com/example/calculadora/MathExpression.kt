package com.example.calculadora

class MathExpression {
    companion object{
        object RegexExp {
            val mulDivRegex = "(\\d+\\.\\d*|\\d*\\.\\d+|\\d+)[*/]{1}[+-]{0,1}(\\d+\\.\\d*|\\d*\\.\\d+|\\d+)".toRegex();
            val addSubRegex = "(\\d+\\.\\d*|\\d*\\.\\d+|\\d+)[+-]{1}(\\d+\\.\\d*|\\d*\\.\\d+|\\d+)".toRegex();
            val openParenthesesRegex = "\\(".toRegex();
            val closeParenthesesRegex = "\\)".toRegex();
            val finalResult = "^[-+]{0,1}(\\d+\\.\\d*|\\d*\\.\\d+|\\d+)[-+*/]*$".toRegex();
            val numberRegex = "(\\d+\\.\\d*|\\d*\\.\\d+|\\d+)".toRegex();
            val numberSignedRegex = "([+-]{0,1}(\\d+\\.\\d*|\\d*\\.\\d+|\\d+))".toRegex();
            val matchMissNumber = "[\\(]+[+-]{0,1}(\\d+\\.\\d*|\\d*\\.\\d+|\\d+)[+-]{0,1}[\\)]+".toRegex();
            val operationSimbols = "[-+*/]".toRegex();
            val multSinal = "[-+]{2}".toRegex();

        }


        private fun calculateMulDiv(expression: String): String {
            val match = RegexExp.mulDivRegex.findAll(expression);
            val currentExpression = match.first().value;
            val matchNumber = RegexExp.numberSignedRegex.findAll(currentExpression)
            val firstNumber = matchNumber.first().value;
            val secondNumber =  matchNumber.elementAt(1).value

            var result = when (RegexExp.operationSimbols.findAll(currentExpression).first().value) {
                "*" -> (firstNumber.toDouble() * secondNumber.toDouble()).toString()
                "/" -> (firstNumber.toDouble() / secondNumber.toDouble()).toString()
                else -> throw Exception("Expressão mal formulada");
            }
            return searchExpressions(expression.replace(currentExpression, result));

        }
        private fun calculateAddSub(expression: String): String {
            val match = RegexExp.addSubRegex.findAll(expression);
            val currentExpression = match.first().value;

            val matchNumber = RegexExp.numberSignedRegex.findAll(currentExpression)

            val firstNumber = matchNumber.first().value;
            val secondNumber = matchNumber.elementAt(1).value

            var result = when (RegexExp.operationSimbols.findAll(currentExpression).first().value) {
                "+" -> (firstNumber.toDouble() + secondNumber.toDouble()).toString()
                "-" -> (firstNumber.toDouble() + secondNumber.toDouble()).toString()
                else -> throw Exception("Expressão mal formulada");
            }
            return searchExpressions(expression.replace(currentExpression, result));

        }

        private fun searchParentheses(expression: String): String {

            val matchOpen = RegexExp.openParenthesesRegex.findAll(expression);
            val matchClose = RegexExp.closeParenthesesRegex.findAll(expression);
            val open = matchOpen.first().range.first;

            var nextClose:Int;
            var expBetween:String;

            var openCount:Int;

            var iterator:Int = 0;
            do{
                nextClose = matchClose.elementAt(iterator).range.first;
                expBetween = expression.subSequence(open + 1, nextClose).toString();
                openCount = RegexExp.openParenthesesRegex.findAll(expBetween).count();

            }while ( openCount != iterator++);

            expBetween = expression.subSequence(open + 1, matchClose.elementAt( openCount ).range.first   ).toString();

            val nExp = expression.replace("($expBetween)", searchExpressions(expBetween));

            return searchExpressions(nExp);

        }
        private fun sinalGame(expression: String):String{
            val matchSinals = RegexExp.multSinal.findAll(expression);
            val sinals = matchSinals.first().value;
            val first = sinals[0];
            val second = sinals[1];
            return if(first == second)
                searchExpressions(expression.replaceFirst(sinals,"+"));
            else
                searchExpressions(expression.replaceFirst(sinals,"-"));

        }
        private fun fixParentheses(expression: String):String{
            val matchOpen = RegexExp.openParenthesesRegex.findAll(expression);
            val matchClose = RegexExp.closeParenthesesRegex.findAll(expression);
            val diff = matchOpen.count() - matchClose.count();

            if(diff == 0){
                return expression;
            }
            var nExpression= expression;
            if(diff < 0){
                repeat(diff * -1){
                    nExpression = "($nExpression";
                }
            }else if(diff > 0){
                repeat((diff)) {
                    nExpression = "$nExpression)";
                }
            }

            return fixParentheses(nExpression);
        }
        private fun removeMissingParentheses(expression: String):String{
            if(!RegexExp.matchMissNumber.containsMatchIn(expression)){
                return expression;
            }
            val matchMissNumber = RegexExp.matchMissNumber.findAll(expression);
            var subExp = matchMissNumber.first().value;
            var nExpression = expression;
            var whithoutParentheses = subExp.replaceFirst(RegexExp.openParenthesesRegex,"").replaceFirst(RegexExp.closeParenthesesRegex,"")

            nExpression = nExpression.replace(subExp ,
                whithoutParentheses
            )

            return removeMissingParentheses(nExpression);
        }
        fun getResult(expression: String):Double{
            var exp:String = "($expression)";
            exp = fixParentheses(exp)
            exp = removeMissingParentheses(exp)
            return RegexExp.numberSignedRegex.findAll(
                searchExpressions(exp)
            ).first().value.toDouble();
        }
        private fun searchExpressions(expression: String): String {

            return when{
                RegexExp.multSinal.containsMatchIn(expression) -> sinalGame(expression);
                RegexExp.finalResult.containsMatchIn(expression) -> expression;
                RegexExp.openParenthesesRegex.containsMatchIn(expression) || RegexExp.closeParenthesesRegex.containsMatchIn(expression) -> searchParentheses(expression);
                RegexExp.mulDivRegex.containsMatchIn(expression)-> calculateMulDiv(expression);
                RegexExp.addSubRegex.containsMatchIn(expression) -> calculateAddSub(expression);
                else -> throw Exception("Expressão mal formulada")
            }
        }

    }

}