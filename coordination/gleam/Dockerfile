FROM ubuntu:16.04

RUN apt-get update
RUN apt-get install -y libcurl4-openssl-dev

ADD gleamEssO_CLV /gleamEssO_CLV
ADD opalv_esso_clv_parPerf.exp /opalv_esso_clv_parPerf.exp
ADD opal_ess.mod /opal_ess.mod
ADD kompakt-PhRep-08a.bew /kompakt-PhRep-08a.bew
ADD opal_esso_clv_parPerf.tsk /opal_esso_clv_parPerf.tsk
ADD opal_fixiert.evo /opal_fixiert.evo
ADD ftext_d.txt /ftext_d.txt

RUN chmod +x /gleamEssO_CLV

ENTRYPOINT ["./gleamEssO_CLV", "/opalv_esso_clv_parPerf.exp"]

