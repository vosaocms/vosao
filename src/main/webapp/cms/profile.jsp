<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
  xmlns:ui="http://java.sun.com/jsf/facelets"
  xmlns:h="http://java.sun.com/jsf/html"
  xmlns:f="http://java.sun.com/jsf/core"
  template="WEB-INF/facelets/layout.xhtml">

<ui:define name="head">
</ui:define>

<ui:define name="title">User profile</ui:define>

<ui:define name="body">

<h:form>

<h1>User profile</h1>

<div class="form-row">
    <label>User name</label>
    <h:inputText value="#{profileBean.user.name}" />
</div>

<div class="form-row">
    <label>User email</label>
    <h:inputText value="#{profileBean.user.email}" />
</div>

<div class="form-row">
    <label>Password</label>
    <h:inputSecret value="#{profileBean.password1}" />
</div>
<div class="form-row">
    <label>Retype the password</label>
    <h:inputSecret value="#{profileBean.password2}" />
</div>

<div class="buttons">
    <h:commandButton value="Save" action="#{profileBean.save}" />
</div>

</h:form>

</ui:define>

</ui:composition>
