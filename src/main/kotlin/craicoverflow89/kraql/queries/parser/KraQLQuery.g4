grammar KraQLQuery;

@header
{
    import craicoverflow89.kraql.queries.parser.components.*;
}

// Parser Rules
query returns [KraQLQuery result]
    :   (
            queryInsert {$result = $queryInsert.result;}
        |
            querySelect {$result = $querySelect.result;}
        ) EOF
    ;

queryInsert returns [KraQLQueryInsert result]
    :   'INSERT INTO' tableName = string '(' queryInsertFields ')'
        'VALUES' '()'
        {$result = new KraQLQueryInsert($tableName.text);}
    ;

queryInsertFields returns [ArrayList<String> result]
    @init {ArrayList<String> result = new ArrayList();}
    :   field1 = string {result.add($field1.text);}
        (
            field2 = string {result.add($field2.text);}
        )*
    ;

querySelect returns [KraQLQuerySelect result]
    :   'SELECT' 'term'
        'FROM' tableName = string
        ('WHERE')?
        ('ORDER BY')?
        {$result = new KraQLQuerySelect($tableName.text);}
    ;

string
    :   CHAR+
    ;

// Lexer Rules
WHITESPACE: [ \t\r\n]+ -> skip;
CHAR: .;