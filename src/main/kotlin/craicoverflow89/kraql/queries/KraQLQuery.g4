grammar KraQLQuery;

@header
{
    import craicoverflow89.kraql.components.*;
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
        'VALUES' '(' queryInsertRecords ')'
        {$result = new KraQLQueryInsert($tableName.text, $queryInsertFields.result, $queryInsertRecords.result);}
    ;

queryInsertFields returns [ArrayList<String> result]
    @init {ArrayList<String> fields = new ArrayList();}
    :   field1 = string {fields.add($field1.text);}
        (
            field2 = string {fields.add($field2.text);}
        )*
        {$result = fields;}
    ;

queryInsertRecords returns [ArrayList<String> result]
    @init {ArrayList<String> fields = new ArrayList();}
    :   field1 = string {fields.add($field1.text);}
        (
            field2 = string {fields.add($field2.text);}
        )*
        {$result = fields;}
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