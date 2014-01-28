create or replace package body Easy_Declaration is
--TODO Удалить после переноса кода в java и отладки
  function Quarter(dt in date) return integer is
  begin
    return round((to_char(dt, 'mm') + 1) / 3);
  end;

  function str6(str2 in number, str4 in number, str5 in number) return number is
    result number;
  begin
    if str2 > str4 then
      result := str2 - str5;
    else
      result := str4 - str5;
    end if;
    return result;
  end str6;

  function Get_Decl_Attrs(quartal  in number,
                          currYear in varchar2 default '05')
    return T_Tbl_Easy_Decl_Attrs
    pipelined is
    rec        T_Easy_Decl_Attrs_full;
    qStr1      number;
    qStr2      number;
    qStr3      number;
    qStr4      number;
    qStr5      number;
    qStr6      number;
    qStr7      number;
    --qSumPatent number;
    result     number;
    --I          pls_Integer;
  begin
    rec := T_Easy_Decl_Attrs_full(null, null, null, null, null, null, null);
    select NVL(sum(t.doc_sum), 0)
      into qStr1
      from in_pp t
     where t.akt_num is not null
       and to_char(t.doc_date, 'yy') = currYear
       and to_char(nvl(t.oper_date, t.doc_date), 'mm') <= quartal * 3;
    -- вычесть сумму возвратов за период
    select qStr1 - NVL(sum(t.doc_sum), 0)
      into qStr1
      from out_pp t
     where t.pay_type_id =
           (select pt.pay_type_id
              from pay_types pt
             where pt.type_code = 'ERROR_PAY')
       and to_char(nvl(t.oper_date, t.doc_date), 'yy') = currYear
       and to_char(nvl(t.oper_date, t.doc_date), 'mm') <= quartal * 3; -- сумма возвратов начала года

    -- вычесть
    qStr2 := qStr1 / 10;
    qStr3 := 100;
    select NVL(sum(o.doc_sum), 0)
      into qStr4
      from out_pp o
     where o.pay_type_id = 1
       and to_char(o.doc_date, 'yy') = currYear
       and to_char(o.doc_date, 'mm') < quartal * 3; -- сумма уплаченного патента с начала года

    select NVL(sum(o.doc_sum), 0)
      into qStr5
      from out_pp o
     where o.pay_type_id = 1
       and to_char(o.doc_date, 'yy') = currYear
       and to_char(nvl(o.oper_date, o.doc_date), 'mm') between
           (quartal - 1) * 3 and quartal * 3 - 1;
    for cr in (select t.payfrombegyear, t.paydelta
                 from t_easy_decl t
                where t.iyear = currYear + 2000
                  and t.iquartal = quartal - 1) loop
      qStr5 := qStr5 + nvl(cr.payfrombegyear, 0);
      qStr5 := qStr5 + nvl(cr.paydelta, 0);
    end loop;
    if (qStr2 > qStr4) then
      result := qStr2 - qStr5;
    else
      result := qStr5 - qStr4;
    end if;
    if quartal = 4 then
      qStr6 := 0;
      qStr7 := result;
    else
      qStr6 := result;
      qStr7 := 0;
    end if;
    pipe row(T_Easy_Decl_Attrs_full(qStr1,
                                    qStr2,
                                    qStr3,
                                    qStr4,
                                    qStr5,
                                    qStr6,
                                    qStr7));
    return;
  end;

  function GetNewDeclAttrs(cMonth   in number,
                           begMonth in pls_integer default 6,
                           iYear    in varchar2 default null,
                           iProcent  in number)
    return T_Tbl_T_EASY_DECL_ATTRS_NEW
    pipelined is
    rec      T_EASY_DECL_ATTRS_NEW;
    qStr1    number;
    qStr2    number;
    qStr2_1  number;
    qStr3    number;
    qStr4    number := 0;
    qStr5    number;
    currYear varchar2(4);
    /*    qStr6      number;
    qStr7      number;
    qSumPatent number;
    result     number;
    I          pls_Integer;*/
    calcDate date;
    procent  number := iProcent;
  begin
    currYear := nvl(iYear, to_char(sysdate, 'yyyy'));
    calcDate := to_date(cMonth || ' ' || currYear, 'DD YYYY');
    if (procent is null) then
      if (calcDate > to_date('01.01.2013', 'DD.MM.YYYY')) then
        procent := 5;
      elsif (calcDate > to_date('01.01.2012', 'DD.MM.YYYY')) then
        procent := 7;
      elsif (calcDate > to_date('01.01.2009', 'DD.MM.YYYY')) then
        procent := 8;
      else
        procent := 10;
      end if;
    end if;
    rec := T_EASY_DECL_ATTRS_NEW(null, null, null, null, null, null, null);
    select NVL(sum(t.doc_sum), 0)
      into qStr1
      from in_pp t
     where t.akt_num is not null
       and to_char(t.doc_date, 'yyyy') = currYear
       and to_char(nvl(t.oper_date, t.doc_date), 'mm') <= cMonth;
    -- вычесть сумму возвратов за период
    select qStr1 - NVL(sum(t.doc_sum), 0)
      into qStr1
      from out_pp t
     where t.pay_type_id =
           (select pt.pay_type_id
              from pay_types pt
             where pt.type_code = 'ERROR_PAY')
       and to_char(nvl(t.oper_date, t.doc_date), 'yyyy') = currYear
       and to_char(nvl(t.oper_date, t.doc_date), 'mm') <= cMonth; -- сумма возвратов начала года

    -- налоговая база ВСЕГО - (сумма поступлений за месяц)
    select NVL(sum(t.doc_sum), 0)
      into qStr2
      from in_pp t
     where t.akt_num is not null
       and to_char(t.doc_date, 'yyyy') = currYear
       and to_char(nvl(t.oper_date, t.doc_date), 'mm') > begMonth
       and to_char(nvl(t.oper_date, t.doc_date), 'mm') <= cMonth;
    -- налога по ставке 10%
    qStr2_1 := qStr2;

    -- сумма налога по расчету
    qStr3 := qStr2_1 / 100 * procent;

    for cr in (select t.s3
                 from t_easy_decl_new t
                where t.iyear = currYear
                  and t.imonth = cMonth - 1) loop
      qStr4 := nvl(cr.s3, 0);
    end loop;
    qStr5 := qStr3 - qStr4;
    pipe row(T_EASY_DECL_ATTRS_NEW(qStr1,
                                   qStr2,
                                   qStr2_1,
                                   qStr3,
                                   qStr3,
                                   qStr4,
                                   qStr5));
    return;
  end GetNewDeclAttrs;

  procedure AddDeclaration4Period(quartal in integer, syear in varchar2) is
  begin
    for cr in (select quartal,
                      syear,
                      grossincome str1,
                      amounttax str2,
                      patentnorm str3,
                      accruedcost4patent str4,
                      payfrombegyear str5,
                      paydelta str6,
                      delta4end str7
                 from table(Easy_Declaration.Get_Decl_Attrs(quartal, syear))) loop
      insert into t_easy_decl
        (iquartal,
         iyear,
         grossincome,
         amounttax,
         patentnorm,
         accruedcost4patent,
         payfrombegyear,
         paydelta,
         delta4end)
      values
        (cr.quartal,
         cr.syear + 2000,
         cr.str1,
         cr.str2,
         cr.str3,
         cr.str4,
         cr.str5,
         cr.str6,
         cr.str7);
    end loop;
  end;

  procedure AddDeclaration4PeriodNew(iMonth in integer, iYear in integer) is
    begMonth integer := 0;
  begin
    if iYear = 2007 then
      begMonth := 6;
    end if;
    for cr in (select *
                 from table(Easy_Declaration.GetNewDeclAttrs(iMonth,
                                                             begMonth,
                                                             iYear))) loop
      insert into t_easy_decl_new
        (imonth, iyear, s1, s2, s2_1, s3, s3_1, s4, s5)
      values
        (imonth,
         iyear,
         cr.s1,
         cr.s2,
         cr.s2_1,
         cr.s3,
         cr.s3_1,
         cr.s4,
         cr.s5);
    end loop;
  end;

  procedure calculate(iMonth in pls_integer,
                      iYear  in pls_integer default null) is
    pYear  pls_integer;
    pMonth pls_integer;
  begin
    pYear  := nvl(iYear, to_char(sysdate, 'yyyy'));
    pMonth := nvl(iMonth, to_char(sysdate, 'mm'));
    delete from t_easy_decl_new t
     where t.imonth = pMonth
       and t.iyear = pYear;
    easy_declaration.adddeclaration4periodnew(pMonth, pYear);
    commit;
  end calculate;
begin
  -- Initialization
  null;
end Easy_Declaration;
