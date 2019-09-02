grammar main;

main                     : PARAGRAPH? (rule_definition|rule_constrained|variable|comment|unknown)
                            (PARAGRAPH (rule_definition|rule_constrained|variable|comment|unknown))*
                             PARAGRAPH? unknown? EOF;


comment                  :  STRING? COMMENT unknown?;
variable                 :  (lambda|expression)? AS name?;
rule_definition          :  IF? expression? THEN action?
                         |  IF expression? THEN? action?;

rule_constrained         :  condition_constrained (COMBINATOR? (condition_constrained | condition_expr | expression_simple))* ;

condition_constrained    : (expression_simple? (CONSTRAINT condition_expr?)+ condition_expr?);


expression               : (condition_group|condition|accessor|arithmetic|content);

condition_group          : COMBINATOR? condition_expr (COMBINATOR condition_expr)+;

condition_expr           : (condition|expression_simple);
condition                : expression_simple? (OPERATOR_COMP|(OPERATOR_COMP expression_simple))+ expression_simple?;
expression_simple        : (arithmetic|accessor|content);

accessor                 : (accessor_with|accessor_of);
accessor_of              : ignore_of? content? OF content?;
ignore_of                :   STRING? OF;
accessor_with            : (accessor_of|content)? WITH (condition_group|condition|content)?;

arithmetic               : (accessor|content)* (OPERATOR_ARITH (accessor|content)*)+;


lambda                     : content? lambda_from? lambda_order? accessor_with
                           | content? lambda_from lambda_order? accessor_with?;

lambda_from                : FROM content;
lambda_order               : ORDERED_BY content;



name                     : unknown;
unknown                  : (STRING | LPAREN | RPAREN | WITH_ERROR | COMBINATOR | OPERATOR_ARITH | WITH | OF | CONSTRAINT | IF | THEN | OPERATOR_COMP | AS | COMMENT | FUNCTION | FROM | ORDERED_BY)+;
content                  : (STRING | LPAREN | RPAREN | FUNCTION)+;
text                     : unknown;
action                   : (error);
error                    : unknown? WITH_ERROR
                         | unknown WITH_ERROR?
                         | unknown;

LPAREN                   :  'ʬlparenʬ';
RPAREN                   :  'ʬrparenʬ';
OPERATOR_ARITH           :  'ʬarithmoperatorʬ'[a-zA-Z]+'ʬ'[a-zA-Z0-9_]+;
WITH_ERROR               :  'ʬerrorcodeʬ'[a-zA-Z0-9_]+ (' '* [0-9]*)?;
WITH                     :  'ʬwithʬ'[a-zA-Z0-9_]+;

FROM                     : 'ʬfromʬ'[a-zA-Z0-9_]+;
ORDERED_BY               : 'ʬorderedʬ'[a-zA-Z0-9_]+;


OF                       :  'ʬofʬ'[a-zA-Z0-9_]+;
CONSTRAINT               :  'ʬconstraintʬ'[a-zA-Z]+'ʬ'[a-zA-Z0-9_]+;
IF                       :  'ʬifʬ'[a-zA-Z0-9_]+;
THEN                     :  'ʬthenʬ'[a-zA-Z0-9_]+;
PARAGRAPH                :  'ʬparagraphʬ';                              //map replaced whitespace somewhere else or simply map here with [a-z]+?
OPERATOR_COMP            :  'ʬoperatorʬ'[a-zA-Z0-9_]+'ʬ'[a-zA-Z0-9_]+;
FUNCTION                 :  'ʬfunctionʬ'[a-zA-Z0-9_]+'ʬ'[a-zA-Z0-9_]+;
AS                       :  'ʬasʬ'[a-zA-Z0-9_]+;
COMBINATOR               :  'ʬandʬ'[a-zA-Z0-9_]+
                          | 'ʬorʬ'[a-zA-Z0-9_]+
                          | 'ʬunlessʬ'[a-zA-Z0-9_]+;
COMMENT                  :  'ʬcommentʬ'[a-zA-Z0-9_]+;
STRING                   :  ~('ʬ')+;

