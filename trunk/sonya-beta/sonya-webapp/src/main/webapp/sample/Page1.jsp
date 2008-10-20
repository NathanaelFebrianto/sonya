<?xml version="1.0" encoding="UTF-8"?>
<!-- 
    Document   : Page1
    Created on : 2008. 10. 16, 오후 6:08:37
    Author     : louie
-->
<jsp:root version="1.2" xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html" xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:ui="http://www.sun.com/web/ui">
    <jsp:directive.page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"/>
    <f:view>
        <ui:page id="page1">
            <ui:html id="html1" lang="">
                <f:loadBundle basename="bundle_sample" var="msg"/>
                <ui:head id="head1">
                    <ui:link id="link1" url="/resources/stylesheet.css"/>
                </ui:head>
                <ui:body id="body1" style="-rave-layout: grid">
                    <ui:form id="form1">
                        <ui:table augmentTitle="false" id="table1" paginateButton="true" paginationControls="true"
                            style="left: 96px; top: 96px; position: absolute; width: 450px" title="Table" width="450">
                            <ui:tableRowGroup id="tableRowGroup1" rows="10" sourceData="#{sample$Page1.personList}" sourceVar="currentRow">
                                <ui:tableColumn headerText="#{msg.id}" id="tableColumn1" sort="id">
                                    <ui:staticText binding="#{sample$Page1.staticText1}" id="staticText1" text="#{currentRow.value['id']}"/>
                                </ui:tableColumn>
                                <ui:tableColumn headerText="#{msg.firstName}" id="tableColumn2" sort="firstName">
                                    <ui:staticText id="staticText2" text="#{currentRow.value['firstName']}"/>
                                </ui:tableColumn>
                                <ui:tableColumn headerText="#{msg.lastName}" id="tableColumn3" sort="lastName">
                                    <ui:staticText id="staticText3" text="#{currentRow.value['lastName']}"/>
                                </ui:tableColumn>
                            </ui:tableRowGroup>
                        </ui:table>
                        <ui:table augmentTitle="false" binding="#{sample$Page1.table2}" id="table2" paginateButton="true" paginationControls="true"
                            style="left: 96px; top: 216px; position: absolute; width: 450px" title="Table" width="450">
                            <ui:tableRowGroup id="tableRowGroup2" rows="10" sourceData="#{sample$Page1.defaultTableDataProvider}" sourceVar="currentRow">
                                <ui:tableColumn headerText="column1" id="tableColumn4" sort="column1">
                                    <ui:staticText id="staticText4" text="#{currentRow.value['column1']}"/>
                                </ui:tableColumn>
                                <ui:tableColumn headerText="column2" id="tableColumn5" sort="column2">
                                    <ui:staticText id="staticText5" text="#{currentRow.value['column2']}"/>
                                </ui:tableColumn>
                                <ui:tableColumn headerText="column3" id="tableColumn6" sort="column3">
                                    <ui:staticText id="staticText6" text="#{currentRow.value['column3']}"/>
                                </ui:tableColumn>
                            </ui:tableRowGroup>
                        </ui:table>
                    </ui:form>
                </ui:body>
            </ui:html>
        </ui:page>
    </f:view>
</jsp:root>
