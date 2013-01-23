from django.shortcuts import get_object_or_404, render_to_response, redirect
from django.http import HttpResponseRedirect, HttpResponse, HttpResponseNotFound
from django.core.urlresolvers import reverse
from django.template import RequestContext
from graphgui.models import Question
from django import forms
from django.core.context_processors import csrf
from django.shortcuts import render
from django.contrib.auth.models import User
from django.contrib.auth import authenticate, login
from django.template import Context, Template
from django.contrib.auth import logout
from django.forms.util import ErrorList
import datetime
import sys
from django.utils import timezone
from django.core.mail import send_mail, EmailMessage
from django.conf import settings


class questionForm(forms.Form):
    question = forms.CharField(label="Question")


def index(request):
    #request.session["myQuestions"] = ""
    myQuestions = getMyQuestions(request.session.get("myQuestions"))
    d = {"pageTitle": "Home",
        "searchActive": True,
        "myQuestions": myQuestions,

    }
    return render_to_response('index.html', d, context_instance=RequestContext(request))


def searchHistory(request):
    questions = Question.objects.all()
    d = {"pageTitle": "Search History",
        "historyActive": True,
        "questions": questions,
    }
    return render_to_response('history.html', d, context_instance=RequestContext(request))


def getMyQuestions(questionPKStringList):
    if questionPKStringList == None or questionPKStringList == "":
        return []
    else:
        returnList = []
        for pk in questionPKStringList.split(",,"):
            pk = int(pk)
            question = get_object_or_404(Question, pk=pk)
            returnList.append(question)
        return returnList


def about(request):
    d = {"pageTitle": "Home",
        "aboutActive": True,

    }
    return render_to_response('index.html', d, context_instance=RequestContext(request))


def clearMyHistory(request):
    request.session["myQuestions"] = ""
    return HttpResponseRedirect("/")


def newSearch(request):
    if request.method == 'POST':
        form = questionForm(request.POST)
        if form.is_valid():
            question = form.cleaned_data['question']
            answer = doQuery(question)
            newDBQuestion = Question(question=question, answer=answer)
            newDBQuestion.save()
            currentQuestions = request.session.get("myQuestions")
            if currentQuestions == None or currentQuestions == "":
                currentQuestions = []
            else:
                currentQuestions = currentQuestions.split(",,")

            currentQuestions.append(str(newDBQuestion.pk))
            request.session["myQuestions"] = ",,".join(currentQuestions)
    return HttpResponseRedirect("/")


def doQuery(queryString):
    return "No answer found."


def clearQuestionsFromDB(request):
    if not request.user.is_authenticated():
        return HttpResponseRedirect("/admin")
    else:
        for question in Question.objects.all():
            question.delete()
        request.session["myQuestions"] = ""
    return HttpResponseRedirect("/history")


def markCorrect(request, questionNumber):
    question = get_object_or_404(Question, pk=questionNumber)
    question.correct = True
    question.save()
    return HttpResponseRedirect("/")


def markIncorrect(request, questionNumber):
    question = get_object_or_404(Question, pk=questionNumber)
    question.correct = False
    question.save()
    return HttpResponseRedirect("/")


def unMark(request, questionNumber):
    question = get_object_or_404(Question, pk=questionNumber)
    question.correct = None
    question.save()
    return HttpResponseRedirect("/")