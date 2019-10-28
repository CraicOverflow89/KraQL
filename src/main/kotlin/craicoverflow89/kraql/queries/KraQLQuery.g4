grammar KraQLQuery;

@header
{
    import craicoverflow89.kraql.components.*;
    import java.lang.StringBuffer;
    import java.util.HashMap;
}

// Parser Rules
query returns [KraQLQuery result]
    :   (
            queryCreateTable {$result = $queryCreateTable.result;}
        |
            queryDeleteFrom {$result = $queryDeleteFrom.result;}
        |
            queryDeleteTable {$result = $queryDeleteTable.result;}
        |
            queryInsert {$result = $queryInsert.result;}
        |
            querySelect {$result = $querySelect.result;}
        |
            queryUpdate {$result = $queryUpdate.result;}
        ) EOF
    ;

queryCreateTable returns [KraQLQueryCreateTable result]
    :   'CREATE TABLE' tableName = string
        PAREN1 queryCreateTableFields PAREN2
        {$result = new KraQLQueryCreateTable($tableName.text, $queryCreateTableFields.result);}
    ;

queryCreateTableFields returns [ArrayList<KraQLQueryCreateTableField> result]
    @init {ArrayList<KraQLQueryCreateTableField> fields = new ArrayList();}
    :   field1 = queryCreateTableField {fields.add($field1.result);}
        (
            COMMA field2 = queryCreateTableField {fields.add($field2.result);}
        )*
        {$result = fields;}
    ;

queryCreateTableField returns [KraQLQueryCreateTableField result]
    :   name = string EQUALS type = string
        {$result = new KraQLQueryCreateTableField($name.text, $type.text);}
    ;

queryDeleteFrom returns [KraQLQueryDeleteFrom result]
    :   'DELETE FROM' tableName = string
        clauseWhere
        {$result = new KraQLQueryDeleteFrom($tableName.text, $clauseWhere.result);}
    ;

queryDeleteTable returns [KraQLQueryDeleteTable result]
    :   'DELETE TABLE' tableName = string
        {$result = new KraQLQueryDeleteTable($tableName.text);}
    ;

queryInsert returns [KraQLQueryInsert result]
    :   'INSERT INTO' tableName = string PAREN1 queryInsertFields PAREN2
        'VALUES' PAREN1 queryInsertRecords PAREN2
        {$result = new KraQLQueryInsert($tableName.text, $queryInsertFields.result, $queryInsertRecords.result);}
    ;

queryInsertFields returns [ArrayList<String> result]
    @init {ArrayList<String> fields = new ArrayList();}
    :   field1 = string {fields.add($field1.text);}
        (
            COMMA field2 = string {fields.add($field2.text);}
        )*
        {$result = fields;}
    ;

queryInsertRecords returns [ArrayList<String> result]
    @init {ArrayList<String> fields = new ArrayList();}
    :   field1 = string {fields.add($field1.text);}
        (
            COMMA field2 = string {fields.add($field2.text);}
        )*
        {$result = fields;}
    ;

querySelect returns [KraQLQuerySelect result]
    :   'SELECT' querySelectFields
        'FROM' tableName = string
        clauseWhere
        clauseOrder
        ('LIMIT')?
        {$result = new KraQLQuerySelect($tableName.text, $querySelectFields.result, $clauseWhere.result, $clauseOrder.result);}
    ;

querySelectFields returns [ArrayList<String> result]
    @init {ArrayList<String> fields = new ArrayList();}
    :   (
            field1 = string {fields.add($field1.text);}
            (
                COMMA field2 = string {fields.add($field2.text);}
            )*
        |
            '*'
        )
        {$result = fields;}
    ;

queryUpdate returns [KraQLQueryUpdate result]
    :   'UPDATE' tableName = string
        'SET' queryUpdateFields
        clauseWhere
        {$result = new KraQLQueryUpdate($tableName.text, $queryUpdateFields.result, $clauseWhere.result);}
    ;

queryUpdateFields returns [ArrayList<KraQLQueryUpdatePair> result]
    @init {ArrayList<KraQLQueryUpdatePair> pairs = new ArrayList();}
    :   field1 = string EQUALS value1 = stringQuoteSingle
        {pairs.add(new KraQLQueryUpdatePair($field1.text, $value1.result));}
        (
            COMMA field2 = string EQUALS value2 = stringQuoteSingle
            {pairs.add(new KraQLQueryUpdatePair($field2.text, $value2.result));}
        )*
        {$result = pairs;}
    ;

clauseWhere returns [ArrayList<KraQLQueryCondition> result]
    @init {ArrayList<KraQLQueryCondition> conditions = new ArrayList();}
    :   (
            'WHERE'
            clause1 = clauseWhereAny
            {conditions.add($clause1.result);}
            (
                COMMA clause2 = clauseWhereAny
                {conditions.add($clause2.result);}
            )*
        )?
        {$result = conditions;}
    ;

clauseWhereAny returns [KraQLQueryCondition result]
    :   (
            clauseWhereEquals {$result = $clauseWhereEquals.result;}
        |
            clauseWhereLike {$result = $clauseWhereLike.result;}
        )
    ;

clauseWhereEquals returns [KraQLQueryConditionEquals result]
    :   field = string EQUALS value = stringQuoteSingle
        {$result = new KraQLQueryConditionEquals($field.text, $value.result);}
    ;

clauseWhereLike returns [KraQLQueryConditionLike result]
    :   field = string 'LIKE' value = stringQuoteSingle
        {$result = new KraQLQueryConditionLike($field.text, $value.result);}
    ;

clauseOrder returns [ArrayList<KraQLQueryOrder> result]
    @init {ArrayList<KraQLQueryOrder> order = new ArrayList();}
    :   (
            'ORDER BY'
            clause1 = clauseOrderAny
            {order.add($clause1.result);}
            (
                COMMA clause2 = clauseOrderAny
                {order.add($clause2.result);}
            )*
        )?
        {$result = order;}
    ;

clauseOrderAny returns [KraQLQueryOrder result]
    :   field = string clauseOrderDirection
        {$result = new KraQLQueryOrder($field.text, $clauseOrderDirection.result);}
    ;

clauseOrderDirection returns [KraQLQueryOrderDirection result]
    :   {KraQLQueryOrderDirection direction = KraQLQueryOrderDirection.ASCENDING;}
        (
            'ASC'
        |
            'DESC' {direction = KraQLQueryOrderDirection.DESCENDING;}
        )
        {$result = direction;}
    ;

stringQuoteSingle returns [String result]
    :   {StringBuffer buffer = new StringBuffer();}
        QUOTE_SINGLE
        string1 = string {buffer.append($string1.text);}
        (
            string2 = string {buffer.append(" " + $string2.text);}
        )*
        QUOTE_SINGLE
        {$result = buffer.toString();}
    ;

string
    :   CHAR+
    ;

// Lexer Rules
COMMA: ',';
PAREN1: '(';
PAREN2: ')';
EQUALS: '=';
WHITESPACE: [ \t\r\n]+ -> skip;
CHAR: ~[ ',];
QUOTE_SINGLE: '\'';