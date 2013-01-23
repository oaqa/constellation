from django.db import models


class Question(models.Model):
    question = models.CharField(max_length=300)
    answer = models.CharField(max_length=300)
    correct = models.NullBooleanField()
