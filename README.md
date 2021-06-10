# Welcome to Captcha4j!

## A Spring Boot Project for a Java Image resolution custom captcha.
This project was born from a company request and became a spring boot project


## UML diagrams

Sequence diagram:

```mermaid
sequenceDiagram
Client ->> CaptchaController: get: captcha
CaptchaController ->> CaptchaCustomService: checkCaptchaChallenge
CaptchaCustomService ->> CaptchaController : ok
CaptchaController ->> CaptchaCustomService : create
