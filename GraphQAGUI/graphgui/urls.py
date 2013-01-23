from django.conf.urls import patterns, include, url
from django.views.generic import DetailView, ListView

urlpatterns = patterns('',
    (r'^$', 'graphgui.views.index'),
    (r'^history$', 'graphgui.views.searchHistory'),
    (r'^clearsearchhistory$', 'graphgui.views.clearQuestionsFromDB'),
    (r'^search$', 'graphgui.views.newSearch'),
    (r'^clearmyhistory$', 'graphgui.views.clearMyHistory'),
    (r'^markcorrect/(?P<questionNumber>.*)$', 'graphgui.views.markCorrect'),
    (r'^markincorrect/(?P<questionNumber>.*)$', 'graphgui.views.markIncorrect'),
    (r'^unmark/(?P<questionNumber>.*)$', 'graphgui.views.unMark'),
)